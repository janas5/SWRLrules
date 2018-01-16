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

  state = {query: '', rules: [], isSomeQuery: false, resultsQuantity: 0};

  componentDidMount() {
    let self = this;
    axios({method: 'GET', url: `http://77.55.220.23:4567/sparql/PREFIX%20bc%3A%20%3Chttp%3A%2F%2Fa.com%2Fontology%23%3E%20SELECT%20DISTINCT%20%3Ftype%20WHERE%20%7B%3Fsubject%20%3Fa%20%3Ftype.FILTER(%20STRSTARTS(STR(%3Ftype)%2Cstr(bc%3A))%20)%7D`,
  json: true})
    .then(response => {
      self.setState({rules: response.data, resultsQuantity: response.data.split(/\r\n|\r|\n/).length-1});
      console.log(response.data);
    })
    .catch(error => {
      console.log(error);
    });
    self.setState({isSomeQuery: true});
  }

  executeQuery() {
  //   let self = this;
  //   axios({method: 'GET', url: `http://77.55.220.23:4567/sparql/PREFIX%20bc%3A%20%3Chttp%3A%2F%2Fa.com%2Fontology%23%3E%20SELECT%20DISTINCT%20%3Ftype%20WHERE%20%7B%3Fsubject%20%3Fa%20%3Ftype.FILTER(%20STRSTARTS(STR(%3Ftype)%2Cstr(bc%3A))%20)%7D`,
  // json: true})
  //   .then(response => {
  //     self.setState({rules: response.data});
  //     //self.setState({companies: response.data, pageCount: 2});
  //     console.log(response.data);
  //   })
  //   .catch(error => {
  //     console.log(error);
  //   });
  }

  renderRules(data) {
    let results = data.split("\n");
    let resultsPreprocessed = [];
    results.forEach((element) => {
        resultsPreprocessed.push(element.replace(/[\r\n]/g, '').substring(6, element.length));
    }, this);
    resultsPreprocessed.pop();
    console.log({splitted_results: resultsPreprocessed});
    //this.setState({resultsQuantity: resultsPreprocessed.length});
    return resultsPreprocessed.map((result) => {
         return <Rule details={result} />
       });
  }

  render() {
    console.log(this.state.rules);
    const quantity = this.state.resultsQuantity;
    return (
      <div className="App">
        <header className="App-header">
          <div className="container">
            <h1 className="App-title"><Icon spin name="cog" /><i>ZTISWRL</i></h1>
          </div>
        </header>
        <div className="container">
          <Col xs={6} className="query-form">
            <form onSubmit={this.executeQuery()}>
              <FormGroup bsSize="large">
                <FormControl type="text" placeholder="Wpisz pytanie SPARQL..." />
              </FormGroup>
              <OverlayTrigger placement="left" overlay={tooltip}>
                <Button type="submit" bsStyle="primary">Wyślij zapytanie</Button>
              </OverlayTrigger>
            </form>
          </Col>
          <Col xs={6} className="query-results">
            <h2>Wyniki<span className="query-results-quantity">{quantity}</span></h2>
            {this.state.rules.length !== 0 ? this.renderRules(this.state.rules) : <div className="query-results-warning"><Icon spin name="spinner" /><p>Wykonaj pytanie!</p></div>  }
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
