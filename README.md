Android Clean Architecture Samples
==================================

A project providing demonstrations on how to architect an Android app using Uncle Bob's Clean Architecture approach.

Samples
-------

| Sample                                                                   | Description                                                                                                                                             |
| ------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [mvvm-kotlin-architecturecomponents](mvvm-kotlin-architecturecomponents) | A clean Model-View-ViewModel (MVVM) architecture sample written in Kotlin. Uses Android Architecture Components.                                        |
| [mvvm-architecturecomponents](mvvm-architecturecomponents)               | A clean Model-View-ViewModel (MVVM) architecture sample written in Java. Uses Android Architecture Components.                                          |
| [mvp](mvp)                                                               | A clean Model-View-Presenter (MVP) architecture sample written in Java.                                                                                 |

Sample app overview
-------------------

A simple app for searching GitHub users with search history feature.

#### Screenshots

![](https://i.imgur.com/GJXpIJx.png)
![](https://i.imgur.com/H7LWeV6.png)
![](https://i.imgur.com/N7LIolu.png)

Basic layers overview
---------------------

All samples have the following basic layers:

- UI

  This layer contains presentation logic such as updating UI elements, formatting the data, etc.

- Domain

  This layer contains business logic such as accessing data sources and combining them, data
  processing, etc.

- Data

  This layer contains data access logic such as database connection, REST API call, etc.

License
-------

    Copyright (c) 2018 Andika Wasisto

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.