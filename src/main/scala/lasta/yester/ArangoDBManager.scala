package lasta.yester

import com.cornfluence.proteus.DocumentClient

object ArangoDBManager {
    val driver = DocumentClient("yester")
}
