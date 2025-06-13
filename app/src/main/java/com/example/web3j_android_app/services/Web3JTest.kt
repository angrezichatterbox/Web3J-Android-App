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
    val web3j = Web3j.build(HttpService("https://sepolia.infura.io/v3"))
    val balance = getBalance(web3j, "")
    println("Balance: $balance")
    val latestBlockNumber = getLatestBlockNumber(web3j)
    println("Latest block number: $latestBlockNumber")
    val credentials = Credentials.create("YOUR_PRIVATE_KEY")
    val toAddress = "0x4592d8f8d7b001e72cb26a73e4fa1806a51ac"
    val amountInEther = BigDecimal.valueOf(0.1)
    transferEth(web3j, credentials, toAddress, amountInEther)
    val newBalance = getBalance(web3j, "")
    println("New balance: $newBalance")
    val newLatestBlockNumber = getLatestBlockNumber(web3j)
    println("Latest block number: $latestBlockNumber")

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