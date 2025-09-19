package com.template.apptemplate.ui.screens.dummy

data class LocationTarget(
    val name: String = "",
    val lat: Double,
    val lng: Double,
) {
    companion object {
        fun getList(): List<LocationTarget> {
            return listOf(
                LocationTarget("Kantor Pekanbaru", 0.4881628, 101.4549017),
                LocationTarget("Chub Square Belakang", -6.199411, 106.821869),
                LocationTarget("Monas (JK10)", -6.173079, 106.826931),
                LocationTarget("Kementrian Koordinator (RDSS)", -6.185115, 106.822683),
                LocationTarget("Gedung Bj.Habibie (GD02)", -6.184541, 106.822063),
                LocationTarget("Custom", 0.0, 0.0),
            )
        }
    }
}
