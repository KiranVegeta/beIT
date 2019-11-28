package org.kotlin.track


data class ActivityItems(
    val health: Double,
    val activityName: String?,
    val activityDescription: String?
)

data class ActivityPageDetails(
    val overallHealth: Double,
    val activities: List<ActivityItems>
)