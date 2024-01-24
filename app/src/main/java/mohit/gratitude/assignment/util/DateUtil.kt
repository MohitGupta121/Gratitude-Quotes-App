package mohit.gratitude.assignment.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {

    private val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    private val calendar = Calendar.getInstance()

    fun getCurrentDate(): String {
        return dateFormat.format(calendar.time)
    }

    fun updateDateToPreviousDay(): String? {
        val sevenDaysAgo = Calendar.getInstance()
        sevenDaysAgo.add(Calendar.DAY_OF_YEAR, -7)
        val sevenDaysAgoDate = sevenDaysAgo.time

        // Check if the current date is within the last 7 days
        if (calendar.time.after(sevenDaysAgoDate)) {
            // Update the date to the previous day
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            return dateFormat.format(calendar.time)
        }

        return null
    }

    fun getNextDate(currentDate: String): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        val parsedDate = dateFormat.parse(currentDate)

        parsedDate?.let {
            if (parsedDate.before(calendar.time)) {
                calendar.time = parsedDate
                calendar.add(Calendar.DAY_OF_YEAR, 1)

                if (calendar.time.before(Date())) {
                    return dateFormat.format(calendar.time)
                }
            }
        }

        return currentDate
    }

    fun formatDisplayDate(date: String): String {
        val currentDate =
            SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().time)
        val parsedDate = dateFormat.parse(date)

        return when {
            parsedDate != null -> {
                val formattedDate =
                    SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(parsedDate)

                when {
                    formattedDate == currentDate -> "Today"
                    isYesterday(date) -> "Yesterday"
                    else -> {
                        val sdf = SimpleDateFormat("MMMM", Locale.getDefault())
                        val dayOfMonth =
                            SimpleDateFormat("d", Locale.getDefault()).format(parsedDate)

                        val formattedDay = when {
                            dayOfMonth.endsWith("1") && !dayOfMonth.endsWith("11") -> "${dayOfMonth}st"
                            dayOfMonth.endsWith("2") && !dayOfMonth.endsWith("12") -> "${dayOfMonth}nd"
                            dayOfMonth.endsWith("3") && !dayOfMonth.endsWith("13") -> "${dayOfMonth}rd"
                            else -> "${dayOfMonth}th"
                        }

                        sdf.format(parsedDate) + ", " + formattedDay
                    }
                }
            }
            else -> "Invalid Date"
        }
    }

    private fun isYesterday(date: String): Boolean {
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayDate = dateFormat.format(yesterday.time)
        return date == yesterdayDate
    }
}


