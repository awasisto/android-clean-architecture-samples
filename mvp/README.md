Android Clean Architecture Sample - MVP
=======================================

A clean Model-View-Presenter (MVP) architecture sample written in Java.

Sample app overview
-------------------

A simple app for searching GitHub users with search history feature.

#### Screenshots

![](https://i.imgur.com/GJXpIJx.png)
![](https://i.imgur.com/H7LWeV6.png)
![](https://i.imgur.com/N7LIolu.png)

Package structure
-----------------

![](https://i.imgur.com/wQk9ZcI.png)

Layers overview
---------------

This sample has the following layers:

- UI

  - View

    This layer is passive, updated by the presenter, interacts with the user and forwards all user interaction to the
    presenter.

  - Presenter

    This layer contains presentation logic such as updating UI elements in the view, formatting the data, etc.
    It accesses the domain layer if it needs to read or modify some data.

- Domain

  This layer contains business logic such accessing data sources, data processing, etc.

- Data

  This layer contains data access logic such as database connection, REST API call, etc.

License
-------

    Copyright (c) 2019 Andika Wasisto

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