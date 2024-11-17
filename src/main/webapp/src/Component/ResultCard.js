
import React, {useMemo} from 'react';
import {Card, Elevation} from "@blueprintjs/core";

function ResultCard({ amount, baseCurrency, result, targetCurrency }) {
    const content = useMemo(() => (
        <Card elevation={Elevation.ONE} style={{ marginTop: '1rem' }}>
            <p>
                {amount} {baseCurrency} = {typeof result === 'number' ? result.toFixed(2) : result} {targetCurrency}
            </p>
        </Card>
    ), [result]);

    return content;
}

export default ResultCard;