package com.example.web3j_android_app.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetBalance
import org.web3j.protocol.http.HttpService
import org.web3j.tx.Transfer
import org.web3j.tx.gas.StaticGasProvider
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger

class Web3ViewModel : ViewModel() {

    private val web3j = Web3j.build(HttpService("https://sepolia.infura.io/v3/d30908ae36cd407cb5389661dd926b05"))
    private val credentials = Credentials.create("ba915b9190a2e4ef1a7e3e14f6055ea35eec7f1dea42ccc3abd74562a8201a97")
    private val gasProvider = StaticGasProvider(
        Convert.toWei("20", Convert.Unit.GWEI).toBigInteger(),
        BigInteger.valueOf(3_000_000)
    )

    fun getBalance(address: String, onResult: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().balance
                val inEth = Convert.fromWei(balance.toString(), Convert.Unit.ETHER)
                onResult(inEth.toPlainString())
            } catch (e: Exception) {
                onResult("Error: ${e.message}")
            }
        }
    }

    fun transferEth(to: String, amount: String, onResult: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val txReceipt = Transfer.sendFunds(
                    web3j,
                    credentials,
                    to,
                    BigDecimal(amount),
                    Convert.Unit.ETHER
                ).send()

                onResult("Success! Tx Hash: ${txReceipt.transactionHash}")
            } catch (e: Exception) {
                onResult("Failed: ${e.message}")
            }
        }
    }
}
