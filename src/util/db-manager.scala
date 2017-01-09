
trait DocumentManager {}

object ArangoDBManager extends DocumentManager {}



abstract class DBManager {
    def addDoc: (containerName: String, document: AnyRef, options: AnyRef)
    def getDoc: (containerName: String, docID: String)
    def updateDoc: (containerName: String, docID: String, newDoc: AnyRef)
}
