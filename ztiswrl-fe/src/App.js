import React, { Component } from 'react';
import { Button, Radio, InputGroup, FormGroup, FormControl, Col, Tooltip, OverlayTrigger, ButtonGroup } from 'react-bootstrap';
import { Icon } from 'react-fa';
import axios from 'axios';
import Rule from './components/Rule'
import logo from './logo.svg';
import './App.css';

const tooltip = (
	<Tooltip id="tooltip">
		<strong>Pytanie musi zostać wprowadzone w poprawnym formacie!</strong>
	</Tooltip>
);

//const queryBase = `http://77.55.220.23:4567/sparql/PREFIX%20bc%3A%20%3Chttp%3A%2F%2Fa.com%2Fontology%23%3E%20`;
const queryBase = `http://77.55.220.23:4567`;

class App extends Component {

  state = {savedQuery: '', rules: [], isSomeQuery: false, currentQuery: '', resultsQuantity: 0, currentOption: '/rules', checkedRadioButton: '/rules', isExecutionDisabled: false};

  componentDidMount() {
    let self = this;
    //+`SELECT%20DISTINCT%20%3Ftype%20WHERE%20%7B%3Fsubject%20%3Fa%20%3Ftype.FILTER(%20STRSTARTS(STR(%3Ftype)%2Cstr(bc%3A))%20)%7D'
    axios({method: 'GET', url: queryBase + '/rules',
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
//resultsQuantity: response.data.split(/\r\n|\r|\n/).length-1
  executeQuery = (e) => {
    e.preventDefault();
    let self = this;
    self.setState({currentOption: this.state.checkedRadioButton, savedQuery: this.state.currentQuery.length===0 ? this.state.currentQuery : '/' + this.state.currentQuery });
    axios({method: 'GET', url: queryBase + this.state.currentOption + this.state.savedQuery,
  json: true})
    .then(response => {
      self.setState({rules: response.data, resultsQuantity: response.data.split(/\r\n|\r|\n/).length-1});
      console.log({fetched: response.data, query: queryBase + this.state.currentOption + this.state.currentQuery});
    })
    .catch(error => {
      console.log(error);
    });
  }

  handleQueryChange = (e) => {
    e.preventDefault();
    this.setState({currentQuery: e.target.value});
  }

  handleRadioButtonChange = (e) => {
    this.setState({checkedRadioButton: e.target.value});
  }

  shouldDisableExecution() {
    const queryInInput = this.state.currentQuery;
    const checkedRB = this.state.checkedRadioButton;
    if((/[A-Z]/.test(queryInInput[0]) && checkedRB === "/rules") || (/[a-z]/.test(queryInInput[0]) && checkedRB === "/rules/with")) {
      this.state.isExecutionDisabled=true;
    }
    else this.state.isExecutionDisabled=false;
    console.log("wykonałem się!");
  }

  displayStatus() {
    if (this.state.isSomeQuery===true && this.state.rules.length===0) {
      return <div className="query-results-warning"><Icon spin name="spinner" /><p>Brak wyników!</p></div>;
    }
    else if (this.state.isSomeQuery===false && this.state.rules.length===0) {
      return <div className="query-results-warning"><Icon spin name="spinner" /><p>Wykonaj pytanie!</p></div>;
    }
  }

  // renderRules(data) {
  //   let results = data.split("\n");
  //   let resultsPreprocessed = [];
  //   results.forEach((element) => {
  //       resultsPreprocessed.push(element.substring(0, element.length));
  //   }, this);
  //   resultsPreprocessed.pop();
  //   console.log({splitted_results: resultsPreprocessed});
  //   return resultsPreprocessed.map((result) => {
  //        return <Rule details={result} />
  //      });
  // }

  renderRules(data, prefix) {
    let results = null;
    if (prefix === "/rules/with" || (prefix === "/rules" && this.state.savedQuery === "")) {
      results = data.split("\n");
      let resultsPreprocessed = [];
      results.forEach((element) => {
          resultsPreprocessed.push(element.substring(0, element.length));
      }, this);
      resultsPreprocessed.pop();
      console.log({splitted_results: resultsPreprocessed});
      return resultsPreprocessed.map((result) => {
        return <Rule details={result} />
      });
    }
    else {
      results = data;
      console.log(results);
      return <Rule details={results} />
    }
    // return results.map((result) => {
    //      return <Rule details={result} />
    //    });
  }

  render() {
    //console.log(this.state.currentOption === "/rules/");
    this.shouldDisableExecution();
    const quantity = this.state.resultsQuantity;
    console.log(this.state);
    return (
      <div className="App">
        <header className="App-header">
          <div className="container">
            <h1 className="App-title"><Icon spin name="cog" /><i>ZTISWRL</i></h1>
          </div>
        </header>
        <div className="container">
          <Col xs={6} className="query-form">
            <form onSubmit={this.executeQuery}>
              <FormGroup bsSize="large">
                <FormControl type="text" value={this.state.currentQuery} onChange={this.handleQueryChange} placeholder="Wpisz pytanie SPARQL..." />
              </FormGroup>
              <OverlayTrigger placement="left" overlay={tooltip}>
                <Button type="submit" bsStyle="primary" disabled={this.state.isExecutionDisabled}>Wyślij zapytanie</Button>
              </OverlayTrigger>
              <ButtonGroup className="radio-buttons-form">
                <Radio name="groupOptions" value="/rules" defaultChecked checked={this.state.checkedRadioButton === "/rules"} onChange={this.handleRadioButtonChange}>Reguła</Radio>
                <Radio name="groupOptions" value="/rules/with" checked={this.state.checkedRadioButton === "/rules/with"} onChange={this.handleRadioButtonChange}>Związki klasy</Radio>
              </ButtonGroup>
            </form>
          </Col>
          <Col xs={6} className="query-results">
            <h2>Wyniki{/*<span className="query-results-quantity">{quantity}</span>*/}</h2>
            {this.state.rules.length !== 0 ? this.renderRules(this.state.rules, this.state.currentOption) 
            : this.displayStatus() }
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

//Select%20Distinct%20*%0AWhere%20%7B%20%3Fp%20rdf%3Atype%20owl%3ADatatypeProperty%20%7D
