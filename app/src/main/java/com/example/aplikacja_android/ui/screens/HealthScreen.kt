package com.example.aplikacja_android.ui.screens

import android.annotation.SuppressLint
import co.yml.charts.common.model.Point
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.aplikacja_android.database.models.BodyMeasurements
import com.example.aplikacja_android.ui.composables.Hyperlink
import com.example.aplikacja_android.ui.viewModels.LocalDatabaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DefaultLocale")
@Composable
fun HealthScreen(navController: NavController) {
    val localDatabaseViewModel = LocalDatabaseViewModel.current

    val allWeightCurrentMonth = localDatabaseViewModel.getWeightsForCurrentMonth().observeAsState()
    val macrosCurrentMonth = localDatabaseViewModel.calculateCurrentMonthNutrients().observeAsState()
    val desiredMacrosCurrentMonth = localDatabaseViewModel.calculateDesiredCurrentMonthNutrients().observeAsState()
    val caloriesBurned = localDatabaseViewModel.calculateCaloriesBurnedForMonth().observeAsState()

    val bodyMeasurements = localDatabaseViewModel.getBodyMeasurements().observeAsState()
    val lastWeight = localDatabaseViewModel.getLastDailyWeight().observeAsState()

    var newBodyMeasurements by remember { mutableStateOf<BodyMeasurements?>(BodyMeasurements(0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0)) }

    var addedInitial by remember { mutableStateOf(false) }
    if (!addedInitial && bodyMeasurements.value != null) {
        newBodyMeasurements = bodyMeasurements.value
        addedInitial = true
    }

    val bmi = if(bodyMeasurements.value != null && lastWeight.value != null) {
        val weight = lastWeight.value!!.weight
        val height = newBodyMeasurements!!.height/100
        weight / (height * height)
    } else {
        null
    }

    val bmiColor = when {
        bmi == null -> Color.Gray
        bmi < 18.5 -> Color(0xFF448FFF)
        bmi in 18.5..24.9 -> Color.Green
        else -> Color.Red
    }
    Column (
        modifier = Modifier.padding(16.dp).
        fillMaxSize().
        verticalScroll(rememberScrollState()),
    ){
        Text("Monitor zdrowia", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.align(Alignment.CenterHorizontally))


        Text(
            text = "Wzrost: ${newBodyMeasurements?.height ?: "Brak danych"} cm",
        )

        Text(
            text = "Ostatnia waga: ${lastWeight.value?.weight ?: "Brak danych"} kg",
        )

        Text(
            text = "BMI: ${ if(bmi != null) String.format("%.2f",bmi) else "Brak danych"}",
            color = bmiColor,
        )

        Text(text = "Body measurments edit(in cm):")
        TextField(
            value = newBodyMeasurements?.height.toString(),
            onValueChange = { newHeight ->
                newBodyMeasurements = newBodyMeasurements?.copy(height = newHeight.toDoubleOrNull() ?: newBodyMeasurements?.height ?: 0.0)
            },
            label = { Text("Your Height") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = newBodyMeasurements?.waist.toString(),
            onValueChange = { newWaist ->
                newBodyMeasurements = newBodyMeasurements?.copy(waist = newWaist.toDoubleOrNull() ?: newBodyMeasurements?.waist ?: 0.0)
            },
            label = { Text("Your Waist") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = newBodyMeasurements?.hip.toString(),
            onValueChange = { newHip ->
                newBodyMeasurements = newBodyMeasurements?.copy(hip = newHip.toDoubleOrNull() ?: newBodyMeasurements?.hip ?: 0.0)
            },
            label = { Text("Your hip") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = newBodyMeasurements?.chest.toString(),
            onValueChange = { newChest ->
                newBodyMeasurements = newBodyMeasurements?.copy(chest = newChest.toDoubleOrNull() ?: newBodyMeasurements?.chest ?: 0.0)
            },
            label = { Text("Your chest") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = newBodyMeasurements?.thigh.toString(),
            onValueChange = { newThigh ->
                newBodyMeasurements = newBodyMeasurements?.copy(thigh = newThigh.toDoubleOrNull() ?: newBodyMeasurements?.thigh ?: 0.0)
            },
            label = { Text("Your thigh") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = newBodyMeasurements?.neck.toString(),
            onValueChange = { newNeck ->
                newBodyMeasurements = newBodyMeasurements?.copy(neck = newNeck.toDoubleOrNull() ?: newBodyMeasurements?.neck ?: 0.0)
            },
            label = { Text("Your neck") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = newBodyMeasurements?.bicep.toString(),
            onValueChange = { newBicep ->
                newBodyMeasurements = newBodyMeasurements?.copy(bicep = newBicep.toDoubleOrNull() ?: newBodyMeasurements?.bicep ?: 0.0)
            },
            label = { Text("Your bicep") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = newBodyMeasurements?.desiredWeight.toString(),
            onValueChange = { newDesiredWeight ->
                newBodyMeasurements = newBodyMeasurements?.copy(desiredWeight = newDesiredWeight.toDoubleOrNull() ?: newBodyMeasurements?.desiredWeight ?: 0.0)
            },
            label = { Text("Your Desired Weight") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Button(
            onClick = {
            if(newBodyMeasurements != null) {
                GlobalScope.launch {
                    localDatabaseViewModel.updateBodyMeasurements(newBodyMeasurements!!)
                }
            }
        }) {
            Text("Save changes")
        }

        Spacer(modifier = Modifier.padding(16.dp))
        val pointsData: List<Point> = allWeightCurrentMonth.value?.map { Point(it.date.dayOfMonth.toFloat(), it.weight.toFloat()) } ?: emptyList()

        Text("Waga w tym miesiącu")
        if(pointsData.isEmpty()) {
            Text("Brak danych")
        }else {
            val minYValue = (pointsData.minByOrNull { it.y }?.y ?: 0f)
            val maxYValue = (pointsData.maxByOrNull { it.y }?.y ?: 0f)
            val yRange = (maxYValue - minYValue).coerceAtLeast(1f) // Ensure non-zero range
            val ySteps = 10 // Divide Y-axis into 5 steps
            val yStepSize = yRange / ySteps

            val xAxisData = AxisData.Builder()
                .axisStepSize(10.dp)
                .backgroundColor(Color.Blue)
                .steps(3)
                .labelData { i -> i.toString() }
                .build()

            val yAxisData = AxisData.Builder()
                .steps(10)
                .backgroundColor(Color.Red)
                .labelData {i->(minYValue + i * yStepSize).toInt().toString()}.build()
            val lineChartData = LineChartData(
                linePlotData = LinePlotData(
                    lines = listOf(
                        Line(
                            dataPoints = pointsData,
                            LineStyle(),
                            IntersectionPoint(),
                            SelectionHighlightPoint(),
                            ShadowUnderLine(),
                            SelectionHighlightPopUp()
                        )
                    ),
                ),
                xAxisData = xAxisData,
                yAxisData = yAxisData,
                gridLines = GridLines(),
                backgroundColor = Color.White
            )
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                lineChartData = lineChartData
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))
        //compare desired weight with last weight
        Text("Cel wagowy:")
        if(lastWeight.value != null) {
            val desiredWeight = newBodyMeasurements?.desiredWeight ?: bodyMeasurements.value?.desiredWeight ?: 0.0
            val lastWeightValue = lastWeight.value!!.weight
            val difference = lastWeightValue - desiredWeight
            if(difference > 0) {
                Text("Do osiągnięcia celu pozostało: $difference kg")
            } else {
                Text("Gratulacje! Osiągnąłeś swój cel")
            }
        }
        if(lastWeight.value != null && allWeightCurrentMonth.value?.get(0)?.weight != null) {
            val lastWeightValue = lastWeight.value!!.weight
            val firstWeightValue = allWeightCurrentMonth.value?.get(0)?.weight
            val difference = lastWeightValue - firstWeightValue!!
            if(difference > 0) {
                Text("W tym miesiącu przytyłeś: $difference kg")
            } else {
                Text("W tym miesiącu schudłeś: ${difference * -1} kg")
            }
        }
        Spacer(modifier = Modifier.padding(16.dp))
        //compare desired weight with last weight
        Text("Konsumpcja kalorii:")
        if(macrosCurrentMonth.value != null && desiredMacrosCurrentMonth.value != null) {
            Text("Skonsumowane kalorie:")
            Text("Białko: ${"%.2f".format(macrosCurrentMonth.value!!["bialko"] ?: 0.0)} ")
            Text("Tłuszcz: ${"%.2f".format(macrosCurrentMonth.value!!["tluszcz"] ?: 0.0)} ")
            Text("Węglowodany: ${"%.2f".format(macrosCurrentMonth.value!!["weglowodany"] ?: 0.0)} ")
            Text("Kalorie: ${"%.2f".format(macrosCurrentMonth.value!!["kalorie"] ?: 0.0)} ")
            Text("Cel kalorii:")
            Text("Białko: ${"%.2f".format(desiredMacrosCurrentMonth.value!!["bialko"] ?: 0.0)} ")
            Text("Tłuszcz: ${"%.2f".format(desiredMacrosCurrentMonth.value!!["tluszcz"] ?: 0.0)} ")
            Text("Węglowodany: ${"%.2f".format(desiredMacrosCurrentMonth.value!!["weglowodany"] ?: 0.0)} ")
            Text("Kalorie: ${"%.2f".format(desiredMacrosCurrentMonth.value!!["kalorie"] ?: 0.0)} ")
            Text("Porównanie z celem:")
            Text("Białko: ${"%.2f".format(macrosCurrentMonth.value!!["bialko"]?.minus(desiredMacrosCurrentMonth.value!!["bialko"] ?: 0.0) ?: 0.0)} ")
            Text("Tłuszcz: ${"%.2f".format(macrosCurrentMonth.value!!["tluszcz"]?.minus(desiredMacrosCurrentMonth.value!!["tluszcz"] ?: 0.0) ?: 0.0)} ")
            Text("Węglowodany: ${"%.2f".format(macrosCurrentMonth.value!!["weglowodany"]?.minus(desiredMacrosCurrentMonth.value!!["weglowodany"] ?: 0.0) ?: 0.0)} ")
            Text("Kalorie: ${"%.2f".format(macrosCurrentMonth.value!!["kalorie"]?.minus(desiredMacrosCurrentMonth.value!!["kalorie"] ?: 0.0) ?: 0.0)} ")
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Text("Spalone kalorie:")
        if(caloriesBurned.value != null) {
            Text("Kalorie: ${"%.2f".format(caloriesBurned.value ?: 0.0)} ")
            Spacer(modifier = Modifier.padding(16.dp))
        }
        Text("Przydatne linki:")
        Hyperlink("Treningi","https://pacjent.gov.pl/aktualnosc/8-tygodni-dla-zdrowia")
        Hyperlink("Jak zmierzyc cisnienie krwi","https://www.youtube.com/watch?v=qlaEHk8WvJY")
        Hyperlink("Jak zmierzyc poziom cukru","https://www.youtube.com/watch?v=v2DtFKREAjo")

        Spacer(modifier = Modifier.padding(100.dp))

    }
}


