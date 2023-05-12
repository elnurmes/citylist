import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';

import Header from './components/header/Header';
import About from './components/page/About';
import Cities from './components/city/ViewCities'
import EditCity from './components/city/EditCity';
import Login from './components/auth/Login';
import Logout from './components/auth/Logout';
import NotFound from './components/page/NotFound';

import './bootstrap.min.css';
import './App.css';

function App () {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    if(sessionStorage.getItem('token') !== null){
      setIsAuthenticated(true);
    }
  }, [])

  return (
    <Router>
    <div className="App">
      <Header isAuthenticated={isAuthenticated} setIsAuthenticated={setIsAuthenticated} />
      <div>
        <Switch>
          <Route exact path="/" render={(props) => (<Cities {...props} isAuthenticated={isAuthenticated} setIsAuthenticated={setIsAuthenticated} />)} />
          <Route exact path="/login" render={(props) => (<Login {...props} isAuthenticated={isAuthenticated} setIsAuthenticated={setIsAuthenticated} />)} />
          <Route exact path="/logout" render={(props) => (<Logout {...props} isAuthenticated={isAuthenticated} setIsAuthenticated={setIsAuthenticated} />)} />
          <Route exact path="/edit/:id" render={(props) => (<EditCity {...props} isAuthenticated={isAuthenticated} setIsAuthenticated={setIsAuthenticated} />)} />
          <Route exact path="/about" component={About} />
          <Route component={NotFound} />
        </Switch>
      </div>
    </div>
    </Router>
  );
}

export default App;
