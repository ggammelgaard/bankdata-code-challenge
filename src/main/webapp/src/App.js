import React from 'react';
import './App.css';
import {
  Button,
  Card,
  FormGroup,
  HTMLSelect,
  NumericInput,
  Intent,
  Elevation,
  H5
} from "@blueprintjs/core";
import "@blueprintjs/core/lib/css/blueprint.css";
import "@blueprintjs/icons/lib/css/blueprint-icons.css";
import ResultCard from "./Component/ResultCard";

function App() {
  const [amount, setAmount] = React.useState('');
  const [baseCurrency, setBaseCurrency] = React.useState('DKK');
  const [targetCurrency, setTargetCurrency] = React.useState('USD');
  const [result, setResult] = React.useState(null);

  const currencies = ['USD', 'DKK'];

  const handleExchange = async () => {
    try {
      const response = await fetch(
          `http://localhost:8080/exchange/${baseCurrency}/${targetCurrency}?amount=${amount}`,
          {method: "POST"}
      );
      const data = await response.json();
      setResult(data);
    } catch (error) {
      console.error('Error fetching exchange rate:', error);
      setResult('Error fetching exchange rate');
    }
  };

  return (
      <div className="main-window">
        <Card elevation={Elevation.FOUR} style={{ width: '400px' }}>
          <H5>Currency Converter</H5>
          <FormGroup
              label="Amount"
          >
            <NumericInput
                id="amount-input"
                value={amount}
                onValueChange={(num, str) => setAmount(str)}
                fill
                allowNumericCharactersOnly={true}
                placeholder="Enter amount"
            />
          </FormGroup>

          <FormGroup
              label="From Currency"
          >
            <HTMLSelect
                id="base-currency"
                value={baseCurrency}
                onChange={(e) => setBaseCurrency(e.target.value)}
                fill
                options={currencies}
            />
          </FormGroup>

          <FormGroup
              label="To Currency"
          >
            <HTMLSelect
                id="target-currency"
                value={targetCurrency}
                onChange={(e) => setTargetCurrency(e.target.value)}
                fill
                options={currencies}
            />
          </FormGroup>

          <Button
              intent={Intent.PRIMARY}
              onClick={handleExchange}
              fill
              large
          >
            Convert
          </Button>

          {result !== null && (
              <ResultCard
                  amount={amount}
                  baseCurrency={baseCurrency}
                  result={result}
                  targetCurrency={targetCurrency}
              />
          )}
        </Card>
      </div>
  );
}

export default App;