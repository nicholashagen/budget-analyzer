Budget Analyzer
===============

The budget analyzer is a Grails-based web application that uses jQuery Mobile to construct the UI.  Its main purpose is
to provide an estimated running balance in the future based on all available budgets, bills, deposits, payments, etc.
While most budget systems provide the ability to show you how much you have spent on a particular budget, you never have
a way of knowing based on the budget and bills I have, how much money will I have in 2 weeks, as an estimate.  This
application provides that capability by taking your current balance into account and reflecting upcoming transactions
against it.  This way you always know how much money you may or may not have at a given time in the future.  This is
especially useful when you want to purchase something but are not sure if you will have enough money to cover you until
your next paycheck.  With this application, you simply update your balance and check the associated date.

# Downloading

```
#> git clone <url>
```

# Setup

Before using this application, you must configure certain parts of it.

First, open ``grails-app/conf/BootStrap.groovy``.  If you want to pre-initialize your budget items, simply update this
file following the samples given.  Otherwise, simply remove those samples.

Second, open ``grails-app/conf/Config.groovy``.  Scroll down to the Grails mail service.  One of the features of this 
application is the ability to automatically download your balance every hour from a service you provide/configure.  
Along with downloading your balance, it will check automatically if at any point in the next 30 days your balance would 
fall below a $100 threshold.  If that would occur, it automatically emails you.  If you want the application to perform
this and email you anytime the threshold is exceeded, simply setup the Grails mail service (see the Grails mail plugin for
examples).  Then, configure budget threshold settings below it to set the email addreses.

Third, open ``grails-app/conf/spring/resources.groovy``.  This is where you can configure or create your own service to
download your current balance.  By default, there are two loaders.  The first is a remote loader that simply downloads a
JSON file containing a ``balance`` node and a ``date`` node.  This may be used if you can setup a remote system that
downloads your balance.  For example, I use Phantom JS on a remote system that logs into my bank, extracts the balance
via jQuery, and writes a JSON file to my webserver.  The second type of loader is an external remote loader that launches 
Phantom JS with a given script to download the balance.  This expects Phantom JS to be running on the same system.

# Running

```
#> grails run-app
```

# Viewing

Simply open your browser to ``http://localhost:8080/budget-analyzer``.  This will open the overview controller by
default.  If you did not use the auto-download balance option, you can click on the balance to open a dialog allowing
you to set the current balance.

# More Information

For more information, see my Blog series:

http://www.znetdevelopment.com/blogs/2013/01/23/budget-analyzer-application-server-side-via-grails/
http://www.znetdevelopment.com/blogs/2013/01/24/budget-analyzer-application-client-side-via-jquery-mobile/

# Screenshot

![Screenshot](https://raw.github.com/nicholashagen/budget-analyzer/master/screenshot.png)
