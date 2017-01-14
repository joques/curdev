object Yester {
    def main(args: Array[String]) {
        println("welcome to Yester... The resource management micro service...")
        val topicList = List("find-users-req", "create-users-req")
        val yProducer = new Producer[String]()
        val yConsumer = new Consumer(topicList)
        val smt = yConsumer.read()
        for (singleMsg <- smt){
            println(singleMsg.message)
        }
    }
}
