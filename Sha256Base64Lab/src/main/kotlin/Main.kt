import java.security.MessageDigest
import java.util.*

fun sha256(input: ByteArray): ByteArray {
    val md = MessageDigest.getInstance("SHA-256")
    return md.digest(input)
}

fun decodeBase64(str: String): ByteArray {
    return Base64.getDecoder().decode(str)
}

fun addByte(x: Byte, arr: ByteArray) : ByteArray {
    val newArr = ByteArray(arr.size + 1)
    newArr[0] = x
    for (i in 1..newArr.lastIndex) {
        newArr[i] = arr[i - 1]
    }
    return newArr
}


fun countHash(l : ByteArray?, r : ByteArray?) : ByteArray? {
    if(l == null && r == null) {
        return null
    }
    val lenL = l?.size ?: 0
    val lenR = r?.size ?: 0
    val res = ByteArray(lenL + lenR + 2)
    res[0] = 1
    for (i in 0 until lenL) {
        res[i + 1] = l?.get(i)!!
    }
    res[lenL + 1] = 2
    for (i in 0 until lenR) {
        res[i + lenL + 2] = r?.get(i)!!
    }
    return res
}


fun main() {
    val h = readLine()!!.toInt()
    val rt = readLine()!!
    val rootHash = if (rt == "null") null else decodeBase64(rt)
    val amountOfBlocks = readLine()!!.toInt()
    repeat(amountOfBlocks) {
        val (idIn, dataIn) = readLine()!!.split(" ")
        val id = idIn.toInt()
        var myHash = if (dataIn == "null") null else sha256(addByte(0, decodeBase64(dataIn)))
        val myHashPoses = ArrayDeque<Char>()
        for (i in 0 until h) {
            myHashPoses.add(if((id / (1 shl i)) % 2 == 0) 'l' else 'r')
        }
        repeat(h) {
            val nData = readLine()!!
            val neighbourHash = if (nData == "null") null else decodeBase64(nData)
            val iAmLeft = myHashPoses.pop() == 'l'
            myHash = if(iAmLeft) {
                val bt = countHash(myHash, neighbourHash)
                if (bt == null) {
                    null
                } else {
                    sha256(bt)
                }
            } else {
                val bt = countHash(neighbourHash, myHash)
                if (bt == null) {
                    null
                } else {
                    sha256(bt)
                }
            }
        }
        if(Arrays.equals(myHash, rootHash)) {
            println("YES")
        } else {
            println("NO")
        }
    }
}