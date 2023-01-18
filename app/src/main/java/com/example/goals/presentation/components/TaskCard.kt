package com.example.goals.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.goals.R.drawable
import com.example.goals.R.string
import com.example.goals.domain.models.SubTask
import com.example.goals.domain.models.Task
import com.example.goals.presentation.ui.theme.fonts
import com.example.goals.utils.timeSecondsToString


@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    task: Task,
    onSubTaskIconCheckClick: (SubTask, Task) -> Unit,
    onTaskIconCheckClick: (Task) -> Unit,
) {
    Box(modifier = modifier.background(Color(task.color))) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    tint = Color.Black,
                    painter = painterResource(
                        id = if (task.isCompleted) drawable.checkbox
                        else drawable.checkbox_empty
                    ),
                    contentDescription = stringResource(string.checkbox),
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            onTaskIconCheckClick(task)
                        }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = task.title,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fonts,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough
                        else TextDecoration.None,
                        textAlign = TextAlign.Center
                    ),
                    overflow = TextOverflow.Clip,
                    maxLines = 1,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = task.content,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fonts)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    tint = Color.Black,
                    painter = painterResource(id = drawable.ic_time),
                    contentDescription = stringResource(string.ic_time),
                    modifier = Modifier.size(24.dp).align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(7.dp))
                val timeStart = task.scheduledTimeStart.timeSecondsToString()
                val timeEnd = task.scheduledTimeEnd.timeSecondsToString()
                Text(text = "$timeStart-$timeEnd",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fonts),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                task.subTasks.forEach { subTask ->
                    SubTask(
                        subTask = subTask,
                        onCheckBoxClick = { clickedSubTask ->
                            onSubTaskIconCheckClick(clickedSubTask, task)
                        },
                        modifier = Modifier
                            .padding(top = 15.dp, bottom = 15.dp)
                            .fillMaxWidth(),
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontFamily = fonts,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                }
            }
        }
    }
}