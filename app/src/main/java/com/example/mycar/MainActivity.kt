package com.example.mycar

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mycar.ui.theme.MyCarTheme

class MainActivity : ComponentActivity() {

    private val viewModel = ViewModelCarCompose()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.physicalCycleStart()

        setContent {
            MyCarTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    Image(painter = painterResource(id = R.drawable.back), contentDescription = "", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)

                    Column {
                        TextHeader()
                        MyCarCard(viewModel.positionX)
                        Control()
                    }
                }
            }
        }
    }

    @Composable
    fun Speedometer(speed: MutableState<Float>) {
        val speedKm = (((((4 * speed.value) / 500) * 100) * 3600) / 1000).toInt()
        Card(modifier = Modifier
            .padding(start = 16.dp, bottom = 4.dp, end = 8.dp, top = 12.dp)
            .fillMaxWidth()
            .height(50.dp),

            backgroundColor = Color.White,
            elevation = 10.dp,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.Gray)  ) {

            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally)
            {
                Text(text = "$speedKm км/ч", color = Color(255, 132, 132, 255), fontSize = 30.sp)
            }
        }
    }

    @Composable
    fun MyCarCard(positionX: MutableState<Float>) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(start = 8.dp, bottom = 4.dp, end = 8.dp, top = 12.dp),

            backgroundColor = Color.White,
            elevation = 4.dp,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(2.dp, Color.White))
        {
            Canvas(positionX)
        }
    }

    @Composable
    fun Canvas(positionX: MutableState<Float>) {
        Box(modifier = Modifier.fillMaxSize()) {
            androidx.compose.foundation.Canvas(modifier = Modifier
                .fillMaxSize()
                .padding(top = 243.dp), onDraw = {

                val valueBek0 = ( 0.005f * positionX.value / 1600).toInt()
                for (i in (valueBek0-1)..(valueBek0+1) ){
                    drawImage(
                        image = ImageBitmap.imageResource(res = resources, id = R.drawable.bek),
                        topLeft = Offset(x = - 0.005f * positionX.value + (i * 1600) - 2, y = -280.dp.toPx()),
                    )
                }

                val valueBek = ( 0.3f * positionX.value / 1000).toInt()
                for (i in (valueBek-1)..(valueBek+1) ){
                    drawImage(
                        image = ImageBitmap.imageResource(res = resources, id = R.drawable.trees4),
                        topLeft = Offset(x = - 0.3f * positionX.value + (i * 1000) - 1, y = -300.dp.toPx()),
                    )
                }

                val valueBek2 = ( 0.7f * positionX.value / 1600).toInt()
                for (i in (valueBek2-1)..(valueBek2+1) ){
                    drawImage(
                        image = ImageBitmap.imageResource(res = resources, id = R.drawable.trees3),
                        topLeft = Offset(x = -  0.7f * positionX.value + (i * 1600) - 1, y = -330.dp.toPx()),
                    )
                }

                val value = (positionX.value / 1600).toInt()
                for (i in (value-1)..(value+1) ){
                    drawImage(
                        image = ImageBitmap.imageResource(res = resources, id = R.drawable.grass2),
                        topLeft = Offset(x = - positionX.value + (i * 1600) - 1, y = 0.dp.toPx()),
                    )
                }
            })
            MyCar(positionX)
        }
    }

    @Composable
    fun MyCar(positionX: MutableState<Float>) {

        Image(painter = painterResource(id = R.drawable.car), contentDescription = "", modifier = Modifier
            .width(200.dp)
            .padding(top = 200.dp, start = 20.dp)
            .rotate(viewModel.angleCar.value))

        Column(modifier = Modifier.offset(x = 50.dp, y = 225.dp)) {
            Image(painter = painterResource(id = R.drawable.circles), contentDescription = "", modifier = Modifier
                .width(28.dp)
                .rotate(positionX.value))
        }

        Column(modifier = Modifier.offset(x = 155.dp, y = 225.dp)) {
            Image(painter = painterResource(id = R.drawable.circles), contentDescription = "", modifier = Modifier
                .width(28.dp)
                .rotate(positionX.value))
        }
    }

    @Composable
    fun Control() {
        val colorRevers = remember { mutableStateOf( Color.LightGray ) }
        val colorGas = remember { mutableStateOf( Color.LightGray ) }
        val colorBrake = remember { mutableStateOf( Color.LightGray ) }

        Row {
            Revers(colorRevers)
            Gas(colorGas)
            Brake(colorBrake)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Revers(colorRevers: MutableState<Color>) {
        Button(modifier = Modifier
            .width(80.dp)
            .height(50.dp)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        viewModel.onRevers()
                        colorRevers.value = Color.Red
                    }
                    MotionEvent.ACTION_UP -> {
                        viewModel.offRevers()
                        colorRevers.value = Color.LightGray
                    }
                    else -> false
                }
                true
            }
            .padding(start = 8.dp, bottom = 4.dp, end = 4.dp, top = 12.dp),

            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            onClick = {},
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, colorRevers.value))
        {
            Text(text = stringResource(R.string.revers), color = Color.Gray, modifier = Modifier, fontSize = 9.sp)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Gas(colorGas: MutableState<Color>) {
        Button(modifier = Modifier
            .width(80.dp)
            .height(50.dp)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        viewModel.onGas()
                        colorGas.value = Color.Red
                    }
                    MotionEvent.ACTION_UP -> {
                        viewModel.offGas()
                        colorGas.value = Color.LightGray
                    }
                }
                true
            }
            .padding(start = 4.dp, bottom = 4.dp, end = 4.dp, top = 12.dp),

            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            onClick = {},
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, colorGas.value))
        {
            Text(text = stringResource(R.string.gas), color = Color.Gray, modifier = Modifier, fontSize = 10.sp)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Brake(colorBrake: MutableState<Color>) {
        Button(modifier = Modifier
            .width(80.dp)
            .height(50.dp)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        viewModel.onBrake()
                        colorBrake.value = Color.Red
                    }
                    MotionEvent.ACTION_UP -> {
                        viewModel.offBrake()
                        colorBrake.value = Color.LightGray
                    }
                }
                true
            }
            .padding(start = 4.dp, bottom = 4.dp, end = 4.dp, top = 12.dp),

            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, colorBrake.value),
            onClick = {})
        {
            Text(text = stringResource(R.string.brake), color = Color.Gray, modifier = Modifier, fontSize = 10.sp)
        }
    }

    @Composable
    fun TextHeader() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, bottom = 4.dp, end = 4.dp, top = 12.dp),
            verticalArrangement = Arrangement.Center)
        {
            Row {
                Text(text = stringResource(R.string.my_car), color = Color.Gray, fontSize = 40.sp, modifier = Modifier.padding(top = 8.dp))
                Speedometer(viewModel.speedStateX)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.scopeCancel()
    }
}

