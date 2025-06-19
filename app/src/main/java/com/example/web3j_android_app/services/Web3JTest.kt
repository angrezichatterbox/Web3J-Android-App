package com.example.web3j_android_app.services

import kotlinx.coroutines.runBlocking
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger


fun main() = runBlocking {
    val web3j = Web3j.build(HttpService("https://sepolia.infura.io/v3/d30908ae36cd407cb5389661dd926b05"))
    val balance = getBalance(web3j, "0x2674e4Ca174eCe3A1F0c888689643c60A0d75738")
    println("Balance: $balance")
    val latestBlockNumber = getLatestBlockNumber(web3j)
    println("Latest block number: $latestBlockNumber")
    val credentials = Credentials.create("ba915b9190a2e4ef1a7e3e14f6055ea35eec7f1dea42ccc3abd74562a8201a97")
    val toAddress = "0x2674e4Ca174eCe3A1F0c888689643c60A0d75738"
    val fromAddress = "0x2674e4Ca174eCe3A1F0c888689643c60A0d75738"
    val amountInEther = BigDecimal.valueOf(0.01)
    transferEth(web3j, credentials, toAddress, amountInEther)
    val newBalance = getBalance(web3j, fromAddress)
    println("New balance: $newBalance")


}


fun getBalance(web3j: Web3j, walletAddress: String): BigInteger {
    val ethGetBalance: EthGetBalance = web3j.ethGetBalance(
        walletAddress,
        DefaultBlockParameterName.LATEST
    ).send()
    return ethGetBalance.balance
}

fun getLatestBlockNumber(web3j: Web3j): BigInteger {
    val ethBlockNumber = web3j.ethBlockNumber().send()
    return ethBlockNumber.blockNumber
}

fun transferEth(
    web3j: Web3j,
    credentials: Credentials,
    toAddress: String,
    amountInEther: BigDecimal
) {
    try {
        val transactionReceipt = Transfer.sendFunds(
            web3j,
            credentials,
            toAddress,
            amountInEther,
            Convert.Unit.ETHER
        ).send()

        println("Transaction successful!")
        println("Tx Hash: ${transactionReceipt.transactionHash}")
    } catch (e: Exception) {
        println("Transaction failed: ${e.message}")
    }
}