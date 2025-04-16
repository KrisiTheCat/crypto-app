import axios from "axios";
import React, {useEffect, useState} from "react";
import { toast } from 'react-toastify';
const api = "http://localhost:8080/api/users";

export const Portfolio = () => {
    const [accountInfo, setAccountInfo] = useState([]);

    useEffect(() => {
        fetchInfo();
    }, [accountInfo]);

    const fetchInfo = async () => {
        axios.get(`${api}/`+1)
            .then((response) => {
                setAccountInfo(response.data);
            })
            .catch((reason) => {
                toast.error("Something went wrong", {toastId: "fetchingAccountInfo"});
            });
    };

    const getTransactions = ()=>{
        return accountInfo.transactions.slice().reverse();
    }
    useState(() => {
        fetchInfo();
    })


    const formatDate = (isoDateString) => {
        const date = new Date(isoDateString);
        
        return new Intl.DateTimeFormat('en-US', {
          month: 'short',    // "Apr"
          day: 'numeric',    // "5"
          hour: 'numeric',   // "11"
          minute: '2-digit', // "30"
          hour12: true,      // AM/PM format
        }).format(date);
    };

    const formatQuantity = (quantity) =>{
        return parseFloat(parseFloat(quantity).toPrecision(4));
    }

    const formatTwoDigits = (a) =>{
        return parseFloat(a).toFixed(2);
    }

    if (accountInfo.length < 1) {
        return <div>Failed to load data.</div>; // Handle error case
    }
    return (
        <div id="portfolio" class="tab-content active">
            <section class="holdings-section">
                <h3>Your Holdings</h3>
                <div class="holdings-grid">
                {accountInfo.holdings.slice(0).map((item) => (
                    <div class="holding-card">
                        <div class="holding-header">
                            <div class="crypto-name">
                                <span class="crypto-icon">
                                    <img alt="" src={`https://aps.fra1.cdn.digitaloceanspaces.com/img/${item.symbol.toLowerCase()}/small.png`}/>
                                </span>
                                {item.name} <span class="crypto-symbol">{item.symbol}</span>
                            </div>
                            <div class="holding-value positive">${formatTwoDigits(item.price)}</div>
                        </div>
                        <div class="holding-details">
                            <div class="holding-amount">{formatQuantity(item.quantity)} {item.symbol}</div>
                            <div class={"holding-change "+item.changeType}>
                                <span>${formatTwoDigits(item.change)}</span>&nbsp;
                                (<span>{formatTwoDigits(item.changePct)}%</span>)</div>
                        </div>
                    </div>
                ))}
                </div>
                {accountInfo.holdings.length === 0 ? "No holdings aquired yet" : ""}
            </section>
            <section class="transactions-section">
                <h3>Recent Transactions</h3>
                <div class="transactions-table">
                    <div class="transaction-header">
                        <div>Asset</div>
                        <div>Amount</div>
                        <div>Price</div>
                        <div>Type</div>
                        <div>Date</div>
                    </div>
                    {getTransactions().map((item) => (
                        <div class="transaction-row">
                            <div class="transaction-holdings">
                                <img alt="" src={`https://aps.fra1.cdn.digitaloceanspaces.com/img/${item.symbol.toLowerCase()}/small.png`}/>
                                {item.name} <span class="crypto-symbol">{item.symbol}</span></div>
                            <div class="transaction-amount">{formatQuantity(item.quantity)} {item.symbol}</div>
                            <div class="transaction-value">${formatTwoDigits(item.price)}</div>
                            <div class={"transaction-type  " + item.method.toLowerCase()}><div>{item.method}</div></div>
                            <div class="transaction-date">{formatDate(item.date)}</div>
                        </div>
                    ))}
                </div>
                {accountInfo.transactions.length === 0 ? "No transactions made yet" : ""}
            </section>
        </div>
    );
};