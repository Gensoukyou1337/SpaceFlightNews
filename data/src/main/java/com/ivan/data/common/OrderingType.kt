package com.ivan.data.common

// I checked the API and the only working sorts they have are by published_at and updated_at.
// Any other ordering parameters just default to sorting by published_at descending.
sealed class OrderingType(val queryValue: String) {
    object PublishedAtAscending : OrderingType("published_at")
    object PublishedAtDescending : OrderingType("-published_at")
    object UpdatedAtAscending : OrderingType("updated_at")
    object UpdatedAtDescending : OrderingType("-updated_at")
}