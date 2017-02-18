Meeting Notes - Feb.17.2017 - 2pm-3:15pm - BA3200

Members Present

-In Person:
    Fredrik
    Jun
    Mansoor
    Mark
    Tanzin

-Online via Slack:
    Steven



Highlights:

-Last in person meeting before reading week

-Excellent discussion on all todo items from last meeting notes
    -Mark printed out the last meeting notes (Monday Feb 13 2017) which came in very handy

-Target user discussion at the end of meeting gave us a good idea on where our product fits




Discussion:
-other purposes this gyroscope functionality can be used (games for patients to retrain movement of hands)

-MVP
    -no autocomplete
    -(backlog) spellcheck using libraries available
    -types one character at a time
    -normal qwerty keyboard with gyroscopic cursor
        -hovering over a character for a time limit inserts it
    -hard date set to finish MVP - March 3 2017

-git modeling
    -merging each others pull requests:
        -whoever comes online should merge a pull request
        -if there are multiple, just merge one or two
        -makes it so that no one is depending on each other so pull requests doesn't start to pile up

    -branch modeling
        -minimum is master brand, then perhaps a development branch
        -maybe a feature branch, keep working in development or feature branch and merge upwards to master
        -never commit directly to master
        -figure out the ideal modeling scheme that will be used
            ex: feature -> development -> master

-assign tasks via git issues
    -the name of the issue will have a priority level [1 being highest priority to 5 being lowest]
    at the beginning of the title.
    -each category of tasks have their own priority level
        ex: [MVP 1] Cursor movement via gyroscope
            [Backlog 3] Hovering animation over letters to show how long before it's inserted

-Asana
    -basically a group calendar
    -do we need to use it?

-documentation and style guidelines
    -everyone should document accordingly with javadocs in mind
    -one member will create the master style guidelines and all code will follow this
        ex: camelCase vs under_score
    -proper documentation leads to nice automatic javadocs in html format with function names and definitions
    neatly presented
    -perhaps put these guidlines and wiki in a github wiki or create a project homepage via github.io

-deliverable 2 video
    -screen record emulator
        -get recording of different axis sensors graphs while phone is moving in hand
    -record real life video demonstration
    -All members give each piece of video to Mansoor who will compile the final video

-why are we doing an Android app?
    -all members familiar with Java

-should we use Balsamiq
    -can provide better and nicely presented artifacts

-plan things to discuss before meeting
    -come with ideas beforehand
    -create a new slack channel for brainstorming
        -post any ideas or questions for next meeting

-member attendance
    -slack was a good idea and serves it purpose, but is no substitute for in person meeting
    -Monday tutorial sessions should be mandatory for all members (even if you show up for 10-15 min)

-Jun's github id not showing up on the repo

-never assign one task to two people
    -members will pick tasks to complete in first come first serve order
    -can maybe post on slack what they are working on or if they need help with their task
    -only one person is responsible for a task, no matter how many people are helping with it

-using the tutorial starter code for the android keyboard
    -will it be fine to use this code and add on top of it or do we create from scratch?
    -Mark will either ask on Piazza, email the TA, or email Joey

-target user
    -people that are deaf and do not have use of their arms
    -what are the competitor products?
        -maybe we are the only product servicing this niche
        -try to see if competitors exist




Tasks that need to be completed:

-over reading week
    -do the basic android tutorial posted on slack
    -test the gyroscope sensor and see what kind of data it outputs
    -think of how we can translate these sensor values to a cursors moving on top of the keyboard

-member roles
    -Android Learner
        -everyone is assigned to this to familiarize themselves with the SDK and android studio IDE




Things we need to solidify:

-exact target user demographic



Before Next Meeting:

-every member should have basic understanding of android and phone's gyroscope sensor



