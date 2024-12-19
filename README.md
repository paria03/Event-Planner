## Event Planner Application

### Overview
This is a web app that helps users create, manage, and view events. It is built using Spring Boot for backend. The app allows users to log in, create events, and even use AI to generate event descriptions!

## Features

#### 1. **User Login**
- Users can create an account and log in.
- After logging in, users can access their event dashboard.

#### 2. **Create Events**
- Logged-in users can create new events by entering the event name, date, location, description, and capacity.
- Users can choose if the event is free or paid.
- Users can also choose if reservations are needed for the event.

#### 3. **AI-Powered Event Descriptions**
- Users can type in keywords to automatically generate an event description.
- The app uses OpenAI to create the description based on the keywords.

#### 4. **Event Dashboard**
- After logging in, users see a dashboard with all the events they created.
- Users can view details for each event, such as the name, date, location, and description.

#### 5. **Event Details**
- Each event has a page that shows all the important information about the event.

### Technologies Used
- **Backend**: Spring Boot (Java framework), Spring Data JPA (for database)
- **Frontend**:  HTML, CSS
- **Database**: MySQL (for storing users and events)
- **AI**: OpenAI API (for generating descriptions)
- **Build Tool**: Maven

### Tutorials
- [w3schools.com](https://www.w3schools.com/icons/fontawesome5_icons_objects.asp)
- [Amigoscode](https://youtu.be/9SGDpanrc8U?si=Ui96oSKezztvkVI1)
- [Amigoscode](https://youtu.be/b9O9NI-RJ3o?si=DOK_1G76yV9Fvej4)
- [Telusko](https://youtu.be/oeni_9g7too?si=wviWJg9Y49a1EGEo)
### How to Run
1. Run the SideProjectEventPlannerApplication.java file
2. Open a browser and go to `http://localhost:8080` to use the app.
