import com.rabbitmq.client.*;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;

public class TranslatorReceiver {
    //uses the Routing strategy

    private static final String EXCHANGE_NAME = "recipientList_translator";
    private static final String bank = "BankOfNorrebro";

    public static void main(String[] argv) throws Exception {

        //receives message like: {"ssn":"123456-6543","creditScore":774,"loanAmount":1234567.0,"loanDuration":"6"}
        // which is binded with the name of the bank

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();

        //create a binding for BankOfNorrebro
        channel.queueBind(queueName, EXCHANGE_NAME, bank);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {

                String message = new String(body, "UTF-8");

                MessageConvertor mc = new MessageConvertor();
                try {
                    mc.processMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println(" [x] Received on key binding'" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

}
