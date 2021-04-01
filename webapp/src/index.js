import React from 'react';
import ReactDOM from 'react-dom';
import { Router, Switch } from 'react-router';
import { Route } from 'react-router-dom';
import { createBrowserHistory } from 'history';
import 'bootstrap/dist/css/bootstrap.min.css';

import './styles/index.css';
import Login from './components/Login';
import Home from './components/Home';

const history = createBrowserHistory();

const routing = (
    <React.StrictMode>
        <Router history={history}>
            <Switch>
                <Route path="/home" component={Home} />
                <Route path="/" component={Login} />
            </Switch>
        </Router>
    </React.StrictMode>
);

ReactDOM.render(routing, document.getElementById('root'));
