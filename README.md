# BankOfNorrebroTranlator
Translator to reformat messages to be understood by BankOfNorrebro. It uses rabbit mq mesaging to retrieve and send data.

It receives messages using the routing strategy which has the bank as the binding key. This means that each bank has a queue to
which only someone who uses the correct queue name and correct binding can send messages.


