# Objective

The purpose of this app is to create a trust-based, real-time platform where strangers can ask each others for favors (that come with reward $$). Users may use it for two reasons;

1. Because they're in need of something immediately 
2. Because they can make money off of it without much inconveniece



# How

The thought process that went into it may be more fitting for a blog post but here's the tech the app relies on:

### Android

The MVP pattern is used to make the code less of a headache to read and write. 

Libraries: 

1. Google Maps API Client 
2. Facebook Graph API Client 
3. Volley Client to handle REST/HTTP communication
4. Socket Client

There's a bit more (UI libs and what not) that'll be shared later on. 

### Backend

1. Node.js
2. Express.js
3.  Socket.io.
4. Mongoose/MongoDB. Database currently hosted at Mlabs.  
5. JWT libs used for authentication

# Progress

A significant amount of time has been spent building it and I'd like to think that an app store launch isn't too far off. 

## Completed

Here are some of the usecases available so far:

1. Make a post requesting a service from those nearby

2. Check the feed for services being requested (based on user's location, updates upon new post)

3. Start a chat with users asking for a service (real-time, works both ways, no crashes, inbox updates, message logged as read/unread)

4. Share their location by sending a map with a pin on current location

   (A Google Map with a pin drop at the location will open upon click)

<p align="middle" float="left">
  <img src="screenshots/Screenshot_1573543334.png" alt="Screenshot_1573543334" width="325" hspace="20"/>

  <img src="screenshots/Screenshot_1573540520.png" alt="Screenshot_1573540520" width="325" hspace="20"/>
</p>

<p align="middle" float="left">
  <img src="screenshots/Screenshot_1573567087.png" alt="Screenshot_1573567087" width="325" hspace="20"/>

  <img src="screenshots/Screenshot_1573567100.png" alt="Screenshot_1573567100" width="325" hspace="20"/>
</p>


<p align="middle" float="left">
  <img src="screenshots/Screenshot_1573540784.png" alt="Screenshot_1573540784" width="325" hspace="20"/>

  <img src="screenshots/Screenshot_1573570101.png" alt="Screenshot_1573570101" width="325" hspace="20"/>
</p>

<p align="middle" float="left">

  <img src="screenshots/Screenshot_1573570802.png" alt="Screenshot_1573570802" width="325" hspace="20"/>

  <img src="screenshots/Screenshot_1573569947.png" alt="Screenshot_1573569947" width="325" hspace="20"/>
</p>

## In Progress

I'm currently working on the profile page to display users live posts. Running into a few bugs, namely the livePosts of the user are emitted before the socket listener is turned on. The livePosts items/card are still being designed as well. 
<p align="middle" float="left">
  <img src="screenshots/Screenshot_1573567254.png" alt="Screenshot_1573567254" width="325" align="center" />
</p>

## To-Do

Below are some of the tasks and usecases that need to be developed before a launch. Like always, there'll be more to add once I get closer to finishing.



#### Mandatory

1. Assign service/favor to user
2. Close/Cancel service when done
3. Rate user after service is closed
4. Show ratings on profile view and add sign out option(i.e remove settings page from bottomnav)
5. Sign in with Google/Uni email
6. Show that account is verified if logged in with uni email
7. Create service to track users location outside app
8. Push backend to cloud.



### Optional

These aren't necessary for the launch of a mvp/beta but are still needed (i think)

1. Create new landing/login page
2. Add onboarding tutorial for users

## Bugs

What's acting up so far:

1. "Scroll up to see new favours" isn't behaving well. 
2. If location permissions weren't granted, feed isn't displayed immediately after they are. App must first restart.
