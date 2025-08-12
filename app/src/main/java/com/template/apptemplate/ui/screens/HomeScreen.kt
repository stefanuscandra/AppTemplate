@file:OptIn(ExperimentalMaterial3Api::class)

package com.template.apptemplate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sdklib.Destination
import com.example.sdklib.DriverInfoResponse
import com.example.sdklib.PaymentResponse
import com.example.sdklib.Pickup
import com.example.sdklib.PromoResponse
import com.example.sdklib.TripDetailsResponse
import com.template.apptemplate.R
import com.template.apptemplate.utils.formatDecimal

@Composable
fun HomeScreen(navController: NavController, viewModel: BookingViewModel = hiltViewModel()) {

    val driverInfo by viewModel.driverInfo.collectAsStateWithLifecycle()
    val promoInfo by viewModel.promoInfo.collectAsStateWithLifecycle()
    val paymentInfo by viewModel.paymentInfo.collectAsStateWithLifecycle()
    val tripInfo by viewModel.tripInfo.collectAsStateWithLifecycle()

    HomeContent(driverInfo, promoInfo, paymentInfo, tripInfo)
}

@Composable
private fun HomeContent(
    driverInfo: DriverInfoResponse?,
    promoInfo: PromoResponse?,
    paymentInfo: PaymentResponse?,
    tripInfo: TripDetailsResponse?,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Booking Detail") },
            )
        }
    ) { pad ->
        LazyColumn(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .background(color = Color.White),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { ProfileView(name = driverInfo?.name.orEmpty(), image = driverInfo?.image.orEmpty()) }
            item {
                val point = if (promoInfo?.point != null) {
                    promoInfo.point.toString()
                } else ""
                PromoView(
                    promoName = promoInfo?.title.orEmpty(),
                    promoPoint = point,
                    image = promoInfo?.image.orEmpty()
                )
            }
            item { HorizontalDivider() }
            item {
                TripInfo(
                    currency = paymentInfo?.currency.orEmpty(),
                    fare = paymentInfo?.tripFare.orEmpty(),
                    promo = paymentInfo?.promoAmount.orEmpty(),
                    total = paymentInfo?.totalAmount.orEmpty()
                )
            }
            item {
                TripView(
                    pickup = tripInfo?.pickup?.name.orEmpty(),
                    destination = tripInfo?.destination?.name.orEmpty()
                )
            }
        }
    }
}

@Composable
private fun ProfileView(name: String, image: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            model = image,
            contentDescription = "profile_picture",
            placeholder = painterResource(R.drawable.ic_launcher_background)
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            val firstName = name.split(" ").first()
            val secondName = name.split(" ").drop(1)
            Text(modifier = Modifier.padding(bottom = 8.dp), text = firstName)
            if (secondName.isNotEmpty()) {
                Text(secondName.joinToString(" "))
            }
        }
    }
}

@Composable
private fun PromoView(promoName: String, promoPoint: String, image: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(12.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(modifier = Modifier.padding(end = 12.dp), verticalArrangement = Arrangement.Center) {
            Text(promoName)
            Text(promoPoint, fontWeight = FontWeight.Bold)
        }
        AsyncImage(
            modifier = Modifier.height(70.dp),
            model = image,
            contentDescription = "promo_image",
            placeholder = painterResource(R.drawable.ic_launcher_background)
        )
    }
}

@Composable
private fun TripInfo(
    currency: String, fare: String, promo: String, total: String,
) {
    Column {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = "Payment Details",
            color = Color.LightGray,
            fontWeight = FontWeight.Bold
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(modifier = Modifier.weight(1f), text = "Trip fare", textAlign = TextAlign.Start)
                Text(
                    modifier = Modifier.weight(1f),
                    text = "$currency ${fare.formatDecimal()}",
                    textAlign = TextAlign.End
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(modifier = Modifier.weight(1f), text = "Promo amount", textAlign = TextAlign.Start)
                Text(
                    modifier = Modifier.weight(1f),
                    text = "$currency ${promo.formatDecimal()}",
                    textAlign = TextAlign.End
                )
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Total amount",
                    textAlign = TextAlign.Start,
                    color = Color.Blue
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = "$currency ${total.formatDecimal()}",
                    textAlign = TextAlign.End,
                    color = Color.Blue
                )
            }
        }
    }
}

@Composable
private fun TripView(pickup: String, destination: String) {
    Column {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = "Trip Details",
            color = Color.LightGray,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "", tint = Color.Green)
                VerticalDivider(modifier = Modifier.height(40.dp))
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "", tint = Color.Red)
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Pickup Location")
                    Text(text = pickup, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.size(24.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Destination Location")
                    Text(text = destination, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    HomeContent(
        driverInfo = DriverInfoResponse(
            id = "DRV-123",
            name = "John Doe abc",
            image = ""
        ),
        promoInfo = PromoResponse(
            id = "PR-123",
            title = "GoFood KFC Promo",
            point = 100,
            image = ""
        ),
        paymentInfo = PaymentResponse(
            currency = "IDR",
            tripFare = "6000",
            promoAmount = "5000",
            totalAmount = "11000"
        ),
        tripInfo = TripDetailsResponse(
            pickup = Pickup("", name = "Bali", lat = 0.0, long = 0.0),
            destination = Destination("", name = "Jakarta", lat = 0.0, long = 0.0)
        )
    )
}
