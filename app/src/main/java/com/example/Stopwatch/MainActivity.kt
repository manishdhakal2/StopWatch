package com.example.Stopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.Stopwatch.ui.theme.StopwatchTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Icon
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            addStopWatch()
        }
    }
}

@Composable
fun addStopWatch() {
    var time by remember { mutableStateOf(0) } //Records elapsed time from start
    var running by remember { (mutableStateOf(false)) } // Variable to store the state of stopwatch
    var showStartButton by remember { mutableStateOf(true) } // Store The state of the start Button
    var borderColor by remember { mutableStateOf(Color.Transparent) } // Store the state of the border color
    var started by remember{mutableStateOf(false)}
    var LapCount by remember { mutableIntStateOf(0) } // Count the Number of Laps
    var LapList = remember { mutableListOf<String>() }
    var listState= rememberLazyListState()

    val scope = rememberCoroutineScope() // Creates a runtime scope
    LaunchedEffect(running) {
        //Increment time by 1sec per sec while the timer is running
        while (running) {
            delay(1000)
            time++
        }
    }

    fun formatDate(time1: Int): String { // Returns the formatted seconds in XX:XX:XX format
        val hours = time1 / 3600
        val minutes = (time1 % 3600) / 60
        val seconds = time1 % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun addLap(timestamp: String) {
        var newLapText = "Lap $LapCount - $timestamp";
        LapList.add(newLapText)


    }

    Column( // The main column layout
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text( // The main timer text
            text = formatDate(time),
            fontSize = 80.sp,
            modifier = Modifier
                .border(8.dp, borderColor, RoundedCornerShape(12.dp))
                .wrapContentSize()
                .padding(20.dp)
        )
        Spacer(modifier = Modifier.height(80.dp)) //adds spacing between timer and startbutton

        if(LapList.size!=0 && started){
            LazyColumn(
                state = listState,
                modifier=Modifier.fillMaxWidth().height(160.dp).border(3.dp, Color.Gray, RoundedCornerShape(12.dp)).padding(6.dp)
            ){
                items(LapList){ item->
                    Text(text=item,
                        fontSize=20.sp)

                }

            }
            Spacer(modifier = Modifier.height(80.dp))
        }
        Row()
        {


            if (showStartButton) {

                Button(
                    shape= RoundedCornerShape(30.dp),
                    // Displays the start button if showStartButton is true
                    onClick = {

                        started=true
                        showStartButton = false
                        running = true
                        borderColor = Color.Green
                    },
                    colors = ButtonDefaults.buttonColors(Color.Green),
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start",
                        tint = Color.White

                    )
                }
            } else {

                Button(
                    shape= RoundedCornerShape(30.dp),
//Displays the stop button if showStartButton is false
                    onClick = {
                        showStartButton = true
                        running = false
                        borderColor = Color.Red
                    },
                    colors = ButtonDefaults.buttonColors(Color.Red),

                    ) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = "Pause",
                        tint = Color.White

                    )
                }
            }


            Spacer(modifier = Modifier.width(20.dp))

            Button(
                shape= RoundedCornerShape(30.dp),
                //The reset Button
                onClick = {
                    started=false
                    time = 0
                    running = false
                    showStartButton = true
                    borderColor = Color.Transparent
                    LapList.clear()
                },
                colors = ButtonDefaults.buttonColors(Color.Gray),

                ) {
                Icon(
                    imageVector = Icons.Default.RestartAlt,
                    contentDescription = "Reset",
                    tint = Color.White

                )
            }

                Spacer(modifier = Modifier.width(25.dp))

                Button(// The Lap Button
                    shape= RoundedCornerShape(30.dp),

                    onClick = {

                        addLap(formatDate(time))
                        LapCount=LapCount+1

                    },
                    colors = ButtonDefaults.buttonColors(Color.Gray),

                    ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = "Lap",
                        tint = Color.White
                    )


                }
            }


        }
    }



    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        StopwatchTheme {
            addStopWatch()
        }
    }







