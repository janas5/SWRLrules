import React, { Component } from 'react';
import { Button, FormGroup, FormControl, Col, Tooltip, OverlayTrigger } from 'react-bootstrap';
import { Icon } from 'react-fa'
import axios from 'axios';
import Rule from './components/Rule'
import logo from './logo.svg';
import './App.css';

const tooltip = (
	<Tooltip id="tooltip">
		<strong>Jeszcze nie zaimplementowano!</strong> Prosimy czekać.
	</Tooltip>
);

class App extends Component {

  state = {query: '', rules: [], isSomeQuery: false};

  componentDidMount() {
    let self = this;
    axios({method: 'GET', url: `http://77.55.220.23:8080/rdf4j-server/repositories`,
  json: true})
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
    axios.get(`http://77.55.220.23:8080/rdf4j-server/repositories`)
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
    //const data = this.executeQuery();
    console.log(this.state.rules);
    return (
      <div className="App">
        <header className="App-header">
          <div className="container">
            <h1 className="App-title"><Icon spin name="cog" /><i>ZTISWRL</i></h1>
          </div>
        </header>
        <div className="container">
          <Col xs={6} className="query-form">
            <form>
              <FormGroup bsSize="large">
                <FormControl type="text" placeholder="Wpisz pytanie SPARQL..." />
              </FormGroup>
              <OverlayTrigger placement="left" overlay={tooltip}>
                <Button bsStyle="primary">Wyślij zapytanie</Button>
              </OverlayTrigger>
            </form>
          </Col>
          <Col xs={6} className="query-results">
            <h2>Wyniki</h2>
            {this.state.rules.length !== 0 ? this.renderRules(this.state.rules.results.bindings) : <div className="query-results-warning"><Icon spin name="spinner" /><p>Wykonaj pytanie!</p></div>  }
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
