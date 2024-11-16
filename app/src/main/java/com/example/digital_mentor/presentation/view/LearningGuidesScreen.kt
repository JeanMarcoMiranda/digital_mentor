package com.example.digital_mentor.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.digital_mentor.domain.model.Course
import com.example.digital_mentor.presentation.intent.LearningGuidesState
import com.example.digital_mentor.presentation.viewmodel.LearningGuidesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LearningGuidesScreen(
    viewModel: LearningGuidesViewModel = koinViewModel(), modifier: Modifier = Modifier
) {
    val viewState by viewModel.viewState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val state = viewState) {
            is LearningGuidesState.CourseSelection -> {
                CourseListContent(state.courses)
            }

            is LearningGuidesState.Error -> Text(
                state.message,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

            LearningGuidesState.Loding -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun CourseListContent(
    courses: List<Course>
) {
    Column {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(courses) { course ->
                CourseCard(course)
            }
        }
    }

}

@Composable
fun CourseCard(
    course: Course
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column {
            AsyncImage(
                model = course.image, contentDescription = course.name
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(course.name)
        }
    }
}