package ch.ipt.viscon.catalogue.api

import com.fasterxml.jackson.annotation.JsonProperty

data class Item(
        @JsonProperty val id: Int,
        @JsonProperty val name: String,
        @JsonProperty val price: Double
)