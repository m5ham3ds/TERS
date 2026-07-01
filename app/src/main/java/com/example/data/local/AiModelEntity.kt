package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.AiModel
import com.example.domain.model.ModelFormat

@Entity(tableName = "models")
data class AiModelEntity(
    @PrimaryKey val id: String,
    val name: String,
    val providerId: String,
    val version: String,
    val capacity: String,
    val format: String,
    val isLocal: Boolean
) {
    fun toDomainModel(): AiModel {
        return AiModel(
            id = id,
            name = name,
            providerId = providerId,
            version = version,
            capacity = capacity,
            format = ModelFormat.valueOf(format),
            isLocal = isLocal
        )
    }
}
