# Hack101 - Android: Lesson 2 #
## Activities ##
----

### Intro and our Second App ###

Hello again! Over the next two tutorials we will be building a To-Do list app. This will teach us about two main topics

1. Activities. Our last app only had one screen. As our apps get more complex we will want more screens (for example, the Facebook app has a newsfeed screen, a screen when you click on a post, a screen for notifications, etc.). These screens are called activities.
2. Saving data. We want our user's to-do items to be saved when they close the app. 

This lesson will cover activities and the next one will cover saving data.

### Activities ###

As I mentioned above, an activity is a new screen of an app. Each activity is a Java class in our `src` folder, and many activities have an `xml` file associated with them. The Java code represents all the logic that takes place in a activity and the `xml` file represents how the screen looks.

When we make a new activity, Android Studio generates an `xml` and `java` file for use with a bunch of code already in it. Let's have a look at that code and understand what it does.

We'll start by make a new app, following the procedure we had last time. I called this app "ToDo list", but you can call it whatever you like. We select "Add Blank Activity". Once Android Studio is done making our app, we will have a `MainActivity.java` file and a `activity_main.xml` file. These are the files in which we specify the logic and the layout of our main activity respectively. It is called out **Main** activity because this is the activity that launches when we open the app. We could change this inside `manifests/AndroidManifest.xml`, where we see that the main activity has been specified as the launcher. 

```xml
        <activity
            android:name=".MainActivity"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
```

All activities of our app need to be registered in `AndroidManifest.xml`. This little snippet declares the activity we have created and the `<intent-filter>` section says it is the activity to open when the user starts the app.

Let's now turn our attention to `MainActivity.java`. To understand all the code in here, we have to understand the **Activity Lifecyle**.

### Activity Lifecycle ###

All activities have a lifecycle. Here is an example:

* When you open an app, the main activity is created and the started. 
* Then you move to another activity the main activity is stopped, and another activity is created then started. 
* When you navigate back to the main activity, the activity you opened is destroyed and the main activity is resumed. 
* Then when you close the app, the main activity is destroyed. 

The image below shows you an activity's lifecycle pictorially:

**Android Activity Lifecycle:**

![activity life cycle ](http://www.android-app-market.com/wp-content/uploads/2012/03/Android-Activity-Lifecycle.png)

Each one of these steps corresponds to a method of the activity.

You'll notice that the main activity is a subclass of ActionBarActivity (if this term is foreign to you, check out this link [here](http://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html)). It is in the ActionBarActivity class (well, actually one of it's superclasses...) that all the methods to do with activity lifecyle are defined. The code automatically generated for the main activity involves overriding some of these methods. let's look at `onCreate()` as an example.

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
```

The first thing we do when we override a lifecycle method is call the super class method. There's a lot of stuff going on behind the scenes here and we don't want to interfere by not executing some important code. The next line here is `setContentView`, and it is in this line that we tell the activity which `xml` file it is associated with. 

Later on, it is in this method that we will be telling our app to load all of our saved data.

We have not overridden any of the other lifecycle methods, as we do not need to, but we could if we wanted to.

The other methods in the main activity are also inherited, but they have to do with the menu bar, and not the activity lifecycle. We will get to them later.

### Back to our App ###

Let's start building our app a little. We'll want our main activity to list all of our to-do items, so let's start there. First we'll edit the file `activity_main.xml` to make our layout. We add a title and also a `ListView`, which makes a scrollable section that we will use to display the to-do items. When we're done, our file should look something like this:


```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools" android:layout_width="match_parent"
    android:layout_height="match_parent" android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    android:paddingBottom="@dimen/activity_vertical_margin" tools:context=".MainActivity">

    <TextView android:id="@+id/title"
        android:text="@string/title"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:textSize="25sp"
        android:gravity="center"
        android:paddingBottom="20dp"/>

    <ListView android:id="@+id/todo_list"
        android:layout_below="@id/title"
        android:layout_width="fill_parent"
        android:layout_height="fill_parent">

    </ListView>
</RelativeLayout>
```

And, since I used a new string, I have to edit my `strings.xml` file and add

```xml
    <string name="title">To-Do:</string>
```

We still have to specify how our list items will look. We'll do that by creating a new file in the `res/layout/ folder called `todo_item.xml`. In this file we specify that we want each list item to be a text view by replacing it's content with this:

```xml
<?xml version="1.0" encoding="utf-8"?>
<TextView xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:paddingBottom="5dp"
        android:textSize="18dp"/>
```
We won't be specifying the text just yet

Great, but how do we show items in our list? Since our items will (eventually) be loaded from stored data, we want to display them programatically (i.e. from one of our Java files). Let's move over to the corresponding Java file for this view, the MainActivity class. We want our ListView to be filled with items whenever the app starts. So where will be put the code to fill the list? In the `onCreate()` method!

First we grab the ListView as an object in our code.

```java
ListView todoList = (ListView) findViewById(R.id.todo_list);
```
Next, we will make a list of to-do items to test our view with (next tutorial, we will create a database and instead be loading our items from here).

Since we will want to access the list of items later on, we declare the list outside of the `onCreate` method. (The `onCreate` method is only called once).

```java
private ArrayList<String> todoListItems = new ArrayList<String>();
```

Inside `onCreate` we make a few dummy list items:

```java
for (int i = 1; i< = 50; i++){
    todoListItems.add("Item "+i); //dummy items for testing
}
```

Next, we need an adapter. An adapter is an object that bridges a view (the XML) with the data that it is to display (the Java variable). We have a list of items to display so we will we using an ArrayAdapter. To use an adapter, we declare it and tell it which items we want to send to the XML file, and how to format these items. We also will want to access the list adapter later, so this is declared outside `onCreate`.

```java
ArrayAdapter<String> todoListAdapter;
```
And inside `onCreate` we initialize it:

```java
// create an array adapter, pass it the item layout and which items we want it to display
todoListAdapter = new ArrayAdapter<String>(
    this, // the context
    R.layout.todo_item, // the list item layout
    todoListItems); // the list content
```

The `R` (R for resource) is a big object that holds all the resources of our app (everything in the `res` folder). The object let's us access all of our resources inside our activities. 

Great, so we have our array adapter, but nothing new shows up on the ListView! This is because we haven't told the list view about the adapter yet. We do that like so:

```java
todoList.setAdapter(todoListAdapter);
```

Let's run our app and have a look!

![full list app](https://raw.githubusercontent.com/hack101/Android-Lesson-2/master/screenshots/full_list.png)

In summary, here is what our main activity looks like now:

```java
public class MainActivity extends ActionBarActivity {

    private ArrayAdapter<String> todoListAdapter;
    private ArrayList<String> todoListItems = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView todoList = (ListView) findViewById(R.id.todo_list);

        for (int i = 1; i<=50; i++){
            todoListItems.add("Item "+i); //dummy items for testing
        }

        todoListAdapter = new ArrayAdapter<String>(
                this, // the context
                R.layout.todo_item, // the list item layout
                todoListItems); // the list content

        todoList.setAdapter(todoListAdapter);
    }
}
```

We're going to revisit this when we have stored data, but for now let's move on to how to make a new activity.

### Adding Another Activity ###

We want to create a new activity that will allow a user create a new item. To do this, right click in the directory view on the side of the project and choose "New -> Activity -> Blank Activity" in Android Studio. I will name this new activity "AddToDoItem", and I will specify it's "Hierearchical Parent" as MainActivity (this means that when a user presses "back" when they are in the AddToDoItem activity, it will take them to the main screen). When we click "Finish", Android Studio will generate files for us. It will create an XML file for the layout of our new activity, as well as a Java file for the logic. It will also edit the `AndroidManifest.xml` file to include the new activity.

![new activity](https://github.com/hack101/Android-Lesson-2/blob/master/screenshots/new_activity.png)

![new activity settings](https://raw.githubusercontent.com/hack101/Android-Lesson-2/master/screenshots/new_activity.png)

Let's edit the XML file to let a user enter a to-do list item. We'll add title, and EditText view and an "Add" button. Our XML will look like this:

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent" android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    android:paddingBottom="@dimen/activity_vertical_margin"
    tools:context="me.amielkollek.todolist.AddToDoItem">

    <TextView android:text="@string/add_a_todo"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:textSize="23dp"
        android:paddingBottom="50dp"
        android:paddingTop="50dp"/>

    <EditText android:id="@+id/add_todo_text"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:hint="@string/add_a_todo_hint"
        android:paddingBottom="10dp"/>

    <Button android:id="@+id/add_todo_button"
        android:layout_marginTop="30dp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="right"
        android:text="@string/add"
   />
</LinearLayout>
```

We are using a linear layout instead of the realtive layout like we did last time. This means that instead of specifying where each item is placed relative to one another, he items are placed in the order in which they appear in the XML (in this case vertically, because of the `android:orientation="vertical"` orientation attribute).


Let's now head over to `AddToDoItem.java` and write some code to grab the entered todo item when the button is pressed. How we do this will look similar to the last tutorial, with one exception. Instead of adding an `onClick` attribute to the XML to call our method, we will put an `onClickListener` in the code. Since we want the button to call our method whenever the activity is open, we will add the `onClickListener` in the `onCreate` method. 

This is what our `onCreate` method will look like:

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do_item);

        // Grab the button view as an object
        Button addButton = (Button) findViewById(R.id.add_todo_button);


        // set an onClick listener
        addButton.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View view){ // here is our on click function
                    // Do Stuff Here!!
                }
            }
        );
    }
```

Once we learn how to save data, this is where we would do that. We're not there yet, so let's put this aside for now. Besides saving the data, we will also want a user to be returned to the main activity. This raises the question, how do we allow users to switch activities?

### Intents ###

Intents are the objects we use to switch between activities. We will use an intent here to take a user back to the main activity once they are done adding an item. We begin by declaring an intent, and declaring which activity we want it to navigate the user to. We declare our intent like so:


```java
    Intent intent = new Intent(this, MainActivity.class);
```

Where `this` tells it the context (which activity we are leaving) and `MainActivity.class` tells it the activity we are going to. For our purposes here, we also want to pass some data along with our intent. We want to send the content of the todo list item to the main activity, so we add it to the intent with `putExtra`

```java
// get the contents of the todo item
String todoItem = ((EditText) findViewById(R.id.add_todo_text)).getText().toString(); 
// add them to the intent
intent.putExtra("TODO_ITEM", todoItem)
```

The first argument acts as a unique identifier for the piece of information I am sending, and the second argument is the information itself. All that's left to do is to actually start the activity! We do this by calling `startActivity(intent)`. In all, our `onCreate` method looks like this:

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do_item);

        Button addButton = (Button) findViewById(R.id.add_todo_button);

        final Intent intent = new Intent(this, MainActivity.class);

        String todoItem = ((EditText) findViewById(R.id.add_todo_text)).getText().toString();
        intent.putExtra("me.amielkollek.todolist.ToDoItem", todoItem);

        addButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(intent);
                    }
                }
        );
    }
```

We declare the intent as `final` as we are using it in the inner class `OnClickListener`. 

Great, we're done with the `AddToDoItem` activity. But there's a problem. There's no way for the user to access this activity! We need another intent. Let's add a button to the main activity:

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools" android:layout_width="match_parent"
    android:layout_height="match_parent" android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    android:paddingBottom="@dimen/activity_vertical_margin" tools:context=".MainActivity">

    <TextView android:id="@+id/title"
        android:text="@string/title"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:textSize="25sp"
        android:gravity="center"
        android:paddingBottom="5dp"/>

    <Button android:id="@+id/add_button"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/title"
        android:text="@string/add_a_todo"/>
    
    <ListView android:id="@+id/todo_list"
        android:layout_below="@id/add_button"
        android:layout_width="fill_parent"
        android:layout_height="fill_parent">

    </ListView>
</RelativeLayout>
```
And let's add a listener to the button so that it can open the `AddToDoItem` activity. Inside the MainActivity class' `onCreate` method:

```java
        Button addButton = (Button) findViewById(R.id.add_button);
        final Intent intent = new Intent(this, AddToDoItem.class);

        addButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(intent);
                    }
                }
        );
```

If we run our app, we'll see that the switching between activities works! 

![list view](https://raw.githubusercontent.com/hack101/Android-Lesson-2/master/screenshots/final_list_view.png)

![add view](https://raw.githubusercontent.com/hack101/Android-Lesson-2/master/screenshots/add_view.png)

That's it for now! In the next tutorial we will create a database of list items and user's will be able to store and delete items!

