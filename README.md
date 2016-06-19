# android-habit-tracker-app
Android app to keep track of the frequency that you complete a habit.

I developed this app as part of the Udacity Android training. To create a habit touch the lower FAB button and type in the habit name. Every day that you complete the habit you touch the "+" button next to it to increase its count. The app will calculate a % based on the times you have completed this habit since the day you added it.

The app implementes a SQLite database and a ContentProvider to manage the creation and updating of habits. It includes two validations:

* It doesn't allow you to create duplicate habits
* It only allows you complete the habit once daily

The app also implementes a RecyclerView.Adapter to bind the data to the Layout.
