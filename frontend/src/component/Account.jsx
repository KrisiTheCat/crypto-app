import axios from "axios";
import React, {useEffect, useState} from "react";
import { toast } from 'react-toastify';
import { Button } from 'react-bootstrap';
const api = "http://localhost:8080/api/users";

export const Account = () => {
    const [accountInfo, setAccountInfo] = useState([]);

    useEffect(() => {
        fetchInfo();
    },[accountInfo]);

    const fetchInfo = async () => {
        axios.get(`${api}/`+1)
            .then((response) => {
                setAccountInfo(response.data);
            })
            .catch((reason) => {
                toast.error("Something went wrong", {toastId: "fetchingAccountInfo"});
            });
    }

    const resetAccount = () => {
        axios.post(api+'/reset/'+1, {})
            .catch((reason) => {
                toast.error("Something went wrong", {toastId: "resetingAccount"});
            });
    };


  return (
    <section class="account-section">
        <div class="account-header">
            <h2 class="welcome-message">Welcome back, <span className="username">{accountInfo.name}</span></h2>
            <p class="portfolio-label">Your portfolio summary</p>
        </div>
        <div class="balance-display">
            <div class="balance-info">
                <div class="balance-label">Total Balance</div>
                <div class="balance-amount">${parseFloat(accountInfo.balance).toFixed(2)}</div>
            </div>
        </div>
        <Button
            size="xs"
            className="resetBtn btn-info"
            onClick={resetAccount}>
            Reset
        </Button>
    </section>
  );
};
