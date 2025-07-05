import app.TcpServer

fun main() {
    println("Start Finncet Server ...")
    TcpServer(port = 9000).start()
}
