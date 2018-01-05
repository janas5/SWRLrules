import React, { Component } from 'react';
import { Button, FormGroup, FormControl, Col } from 'react-bootstrap';
import { Icon } from 'react-fa'
import axios from 'axios';
import Rule from './components/Rule'
import logo from './logo.svg';
import './App.css';

class App extends Component {

  state = {query: '', rules: [], isSomeQuery: false};

  componentDidMount() {
    let self = this;
    axios.get(`http://77.55.220.23:8080/rdf4j-server/protocol`)
    .then(response => {
      self.setState({rules: response.data});
      //self.setState({companies: response.data, pageCount: 2});
      console.log(response.data);
    })
    .catch(error => {
      console.log(error);
    });
    self.setState({isSomeQuery: true});
  }

  executeQuery() {
    let self = this;
    axios.get(`http://77.55.220.23:8080/rdf4j-server/protocol`)
    .then(response => {
      self.setState({rules: response.data});
      //self.setState({companies: response.data, pageCount: 2});
      console.log(response.data);
    })
    .catch(error => {
      console.log(error);
    });
    self.setState({isSomeQuery: true});
  }

  renderRules(data) {
    return data.map((result) => {
      return <Rule details={result} />
    }); 
  }

  render() {
    const data = this.executeQuery();
    console.log(data);
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to React</h1>
        </header>
        <div className="container">
          <Col xs={6} className="query-form">
            <form>
              <FormGroup bsSize="large">
                <FormControl type="text" placeholder="Wpisz pytanie SPARQL..." />
              </FormGroup>
              <Button bsStyle="primary">Wyślij zapytanie</Button>
            </form>
          </Col>
          <Col xs={6} className="query-results">
            <h2>Wyniki</h2>
            {this.state.rules !== [] ? this.renderRules(data) : <div className="query-results-warning"><Icon spin name="spinner" /><p>Wykonaj pytanie!</p></div>  }
          </Col>
        </div>
      </div>
    );
  }
}

export default App;

// <p className="App-intro">
//               To get started, edit <code>src/App.js</code> and save to reload.
//             </p>
