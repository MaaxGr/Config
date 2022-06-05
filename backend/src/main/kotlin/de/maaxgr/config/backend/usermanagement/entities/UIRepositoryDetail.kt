package de.maaxgr.config.backend.usermanagement.entities

@kotlinx.serialization.Serializable
data class UIRepositoryDetail(
    val id: Int,
    val name: String,
    val configs: List<UIRepositoryDetailConfig>
)

@kotlinx.serialization.Serializable
data class UIRepositoryDetailConfig(
    val path: String,
    val accessibleVariants: List<UIRepositoryDetailConfigVariant>
)

@kotlinx.serialization.Serializable
data class UIRepositoryDetailConfigVariant(
    val id: Int,
    val name: String
)
