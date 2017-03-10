# MotionKey

## Iteration 02

 * Start date: February 14, 2017
 * End date: March 8, 2017

## Process

For this phase, we are planning on implementing our minimum viable product - we will produce a functional
motion controlled keyboard that will at the very least allow gyroscope-controlled input with no use for
any form of touch interaction. In order for us to complete this task, we anticipate that over the next
couple of weeks, all team members must familiarize themselves with the android development environment.
We spent the majority of the last iteration conceiving our product - because of this, little time was
actually spent learning the skills and researching the specific algorithms and libraries we would need
in order to actually implement the keyboard.

<br />
#### Roles & responsibilities

| Role | Description | Name |
| --- | --- | --- |
| Android Developer | Responsible for developing the Android Keyboard | Mark |
| Android Developer | Responsible for developing the Android Keyboard | Fredrik |
| Android Developer | Responsible for developing the Android Keyboard | Mansoor |
| Android Developer | Responsible for developing the Android Keyboard | Tanzin |
| Android Developer | Responsible for developing the Android Keyboard | Steven |
| Android Developer | Responsible for developing the Android Keyboard | Jun |
| Meeting Leader | Prepares meeting topics and ensures everyone stays on topic | Mark |
| Minute Taker | Records important information discussed at the meeting | Mansoor |
| Android Researcher | Researches resources related to Android development | Mark |
| Sensor Researcher | Researches resources related to Android development | Mansoor |
| Network Specialist  | Researches how to interface Android Phones to Windows PCs via Internet Protocols | Tanzin |
| UI Researcher | Researches resources related to Android development | Steven |
| Issue Manager | Allocates group resources to existing issues that need attention | Jun |
| Video Editor  | Edits the demo video for deliverable 2 | Mansoor |
| Lead Actor  | Plays the starring role in the video | Tanzin |
| Video Production Team  | Helped produce demo video | Mark |
| Video Production Team  | Helped produce demo video | Tanzin |
| Video Production Team  | Helped produce demo video | Mansoor |
| Video Production Team  | Helped produce demo video | Jun |
| Proof reader  | Proofreads all documents and documentation | Steven |
<br />

## Events

Our first meeting for this iteration occurred on February 14. We chose this particular date because it was a couple of days
before the start of reading week. This gave us ample opportunity to cover our bases, understand the requirements and
expectations from each member over the next couple of weeks as well as the specific tasks that were ahead of us in order
to implement the minimum viable product.

We anticipate that our next meeting will be on February 27, the day after reading week ends. On that day, we can reflect on what has
been completed over the past week and what still needs to be done. We expect that every team member will have at the very least gone
over the Android tutorial that one member found (and subsequently posted as a git issue for each member to complete) which explains
the basic workflow of implementing an android app. We hope to spend the next week or two after that doing an agile programming
style approach to implement the MVP itself because our time will be limited between February 27 and March 10 - the day the iteration
is actually due. 

Furthermore, we would need a functional product a couple of days before that which could be used during
the video production process. We anticipate that we will meet somewhere around March 6 and March 8 in order to actually shoot the
video. This particular rendezvous can also act as a pseudo-meeting which will allow us to reflect on what is yet to be done
before the due date of the iteration.

#### Meeting Highlights
This was our last in-person meeting before reading week. We conducted thorough discussion on all the to-do items that we
believe should be completed by this iteration. Mark printed out both the highlights from the last meeting as well as a
list of to-do items which allowed us to approach the meeting in a more formal and controller manner. We discussed what
would be our exact target user base, by what date each member must have completed the tutorial (which must be done
for each member to be a useful resource during the coding phase of the project), what would be our git branching model
(we decided to do a model that branches development from functional base product as described below), how we would make
use of git issues, how we would track member attendance, what coding style and conventions we will follow, and what are the
specifics of our minimum viable product. We decided that our product MVP is simply a bare bones keyboard that can be controlled
by motion with no extraneous features. This meeting was very productive due to every little detail having been planned beforehand,
which is something we will continue to follow going forward.

#### Artifacts

List/describe the artifacts you will produce in order to organize your team.       

 * Artifacts can be To-do lists, Task boards, schedule(s), etc.
 * We want to understand:
   * How do you keep track of what needs to get done?
   * How do you prioritize tasks?
   * How do tasks get assigned to team members?

#### Git / GitHub workflow



Our git repository has 3 branches. Master, development, and features.

| Branch | Description |
| --- | --- |
| Master | Only that is fully tested, documented. Only the code that we're comfortable with our Professor/TA looking at. |
| Development | Code that is part of the basic MVP, or not fully tested, documented, etc. features |
| Feature ... | A single branch for each modular feature |

The process of working with the repository is 
 * Fork the group repository. 
 * Clone the fork.
 * Make commits on proper branch to the fork repository.
 * Pull request from forked repository to group repository.

A pull request cannot be merged by the person who initiated it.
In order to merge the development branch to the master it must be approved by a minimum of 3 group members, preferably by every member of Team 15.
 
For this milestone we are aiming to work on completing the MVP, as a result we will not fully utilize the feature branch.

We decided to use this workflow for multiple reasons. The master branch ensures that we meet our coding standards.
The development branch allows members to commit code that may not be fully implemented, documented, tested, but still enable other team members who may 
rely on those features to use the latest implementation. The feature branch increases modularity of our design. Modularity is very important to us. It
enables our team to work on multiple features in our backlog simultaneously while avoiding as many merge conflicts as possible.

## Product

#### Goals and tasks

 * Describe your goals for this iteration and the tasks that you will have to complete in order to achieve these goals.
 * Order the items from most to least important.
 * Feel free (but not obligated) to specify some/all tasks as user stories.

#### Artifacts

List/describe the artifacts you will produce in order to present your project idea.

 * Artifacts can be text, code, images, videos, interactive mock-ups and/or any other useful artifact you can think of.
 * Make sure to explain the purpose of each artifact (i.e. Why is it on your to-do list? Why is it useful for your team?)
 * Be concise, yet precise.         
   For example: "Build the website" is not precise at all, but "Build a static home page and upload it somewhere, so that it is publicly accessible" is much clearer.
