package com.example.liveweather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.liveweather.api.NetworkResponse
import com.example.liveweather.api.WeatherModel

@Composable
fun WeatherPage(modifier: Modifier, viewModel: WeatherViewModel) {

    var city by remember { mutableStateOf("") }
    val weatherResult = viewModel.weatherResult.observeAsState()


    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                singleLine = true,
                value = city,
                onValueChange = { city = it },
                label = {
                    Text(text = "Search for any location")
                }
            )
            IconButton(onClick = {
                viewModel.getData(city)
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search for any location"
                )
            }

        }
        when (val result = weatherResult.value) {
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }

            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
                Footer()
            }

            null -> {
                WelcomeContent()
                Footer()
            }
        }
    }
}


@Composable
fun WeatherDetails(data : WeatherModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                modifier = Modifier.size(40.dp)
            )
            Text(text = data.location.name, fontSize = 30.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = data.location.country, fontSize = 18.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = " ${data.current.temp_c} ° C",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "Weather Icon",

            )
        Text(
            text = data.current.condition.text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        Text(text = "Wind",fontSize = 25.sp,fontWeight = FontWeight.SemiBold)
                        Text(
                            text = "${data.current.wind_kph} km/h", fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                    Column {
                        Text(text = "Humidity",fontSize = 25.sp,fontWeight = FontWeight.SemiBold)
                        Text(text = "${data.current.humidity}%", fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Gray)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        Text(text = "Pressure",fontSize = 25.sp,fontWeight = FontWeight.SemiBold)
                        Text(text = "${data.current.pressure_mb} mb", fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Gray)
                    }
                    Column {
                        Text(text = "Visibility",fontSize = 25.sp,fontWeight = FontWeight.SemiBold)
                        Text(text = "${data.current.vis_km} km", fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Gray)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        Text(text = "UV Index",fontSize = 25.sp,fontWeight = FontWeight.SemiBold)
                        Text(
                            text = "${data.current.uv}", fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                    Column {
                        Text(text = "Cloud",fontSize = 25.sp,fontWeight = FontWeight.SemiBold)
                        Text(
                            text = "${data.current.cloud}%", fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        Text(
                            text = "Date",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = data.location.localtime.split(" ")[0],
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }

                    Column {
                        Text(
                            text = "Time",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = data.location.localtime.split(" ")[1],
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun WelcomeContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            modifier = Modifier.size(90.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Check the Weather",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Search for a city to see\nreal-time weather information",
            fontSize = 17.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(35.dp))

        Text(
            text = "🌤️",
            fontSize = 55.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Know the weather before you go",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Temperature • Wind • Humidity • UV Index",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Footer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "LiveWeather 🌤️",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Real-time weather information",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Powered by WeatherAPI",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}