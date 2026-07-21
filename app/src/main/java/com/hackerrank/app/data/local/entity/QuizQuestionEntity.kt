package com.hackerrank.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "quiz_questions",
    foreignKeys = [
        ForeignKey(
            entity = DataStructureEntity::class,
            parentColumns = ["id"],
            childColumns = ["structure_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("structure_id")],
)
data class QuizQuestionEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "structure_id") val structureId: String,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "options_json") val optionsJson: String,
    @ColumnInfo(name = "correct_index") val correctIndex: Int,
    @ColumnInfo(name = "explanation") val explanation: String,
)
