package com.example.web3j_android_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.web3j_android_app.services.Web3ViewModel
import com.example.web3j_android_app.ui.theme.Web3JAndroidAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = Web3ViewModel()

        setContent {
            MaterialTheme {
                Web3Screen(viewModel)
            }
        }
    }
}


@Composable
fun Web3Screen(viewModel: Web3ViewModel) {
    var walletAddress by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var toAddress by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var txStatus by remember { mutableStateOf("") }

    var username by remember { mutableStateOf("") }
    var greeting by remember { mutableStateOf("") }
    var contractAddress by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Ethereum Wallet", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = walletAddress,
            onValueChange = { walletAddress = it },
            label = { Text("Wallet Address") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            viewModel.getBalance(walletAddress) {
                balance = it
            }
        }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Check Balance")
        }

        Text("Balance: $balance ETH", modifier = Modifier.padding(top = 8.dp))

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        OutlinedTextField(
            value = toAddress,
            onValueChange = { toAddress = it },
            label = { Text("Recipient Address") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount (ETH)") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            viewModel.transferEth(toAddress, amount) {
                txStatus = it
            }
        }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Send ETH")
        }

        Text("Status: $txStatus", modifier = Modifier.padding(top = 8.dp))

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // 🧾 CONTRACT UI

        Text("Smart Contract", style = MaterialTheme.typography.headlineSmall)

        Button(onClick = {
            viewModel.deployContract {
                contractAddress = it
            }
        }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Deploy Contract")
        }

        if (contractAddress.isNotEmpty()) {
            Text("Contract Address: $contractAddress", modifier = Modifier.padding(top = 8.dp))
        }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Enter Username") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Button(onClick = {
            viewModel.setUsername(username) {
                txStatus = it
            }
        }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Set Username in Contract")
        }

        Button(onClick = {
            viewModel.greetUser {
                greeting = it
            }
        }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Get Greeting")
        }

        Text("Greeting: $greeting", modifier = Modifier.padding(top = 8.dp))
    }
}
