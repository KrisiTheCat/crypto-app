
import axios from "axios";
import React, {useEffect, useState} from "react";
import { Modal, Button } from 'react-bootstrap';
import { toast } from 'react-toastify';

const api = "http://localhost:8080/api";

export const Market = () => {
    
    const [minValueCrypto, setMinValueCrypto] = useState(0.001);
    const [step, setStep] = useState(0.001);

    const [showModal, setShowModal] = useState(false);
    const [currentTrade, setCurrentTrade] = useState();
    var ableToDoTransaction = true;
    
    const [amount, setAmount] = useState('0.01');
    const [availableBalance, setBalance] = useState(10000);

    const [snapshot, setSnapshot] = useState([]);    
    const [accountInfo, setAccountInfo] = useState([]);
    useEffect(() => {
        fetchCurrSnapshot();
        fetchInfo();
    },[snapshot]);

    const fetchInfo = async () => {
        axios.get(`${api}/users/1`)
            .then((response) => {
                setAccountInfo(response.data);
            })
            .catch((reason) => {
                toast.error("Something went wrong", {toastId: "fetchingAccountInfo"});
            });
    }

    const displayBalance = ()=>{
        if(currentTrade.action === "BUY") return '$'+formatMoney(availableBalance);
        else return formatQuantity(availableBalance) + " " + currentTrade.crypto.symbol;
    }


    const fetchCurrSnapshot = async () => {
        axios.get(`${api}/snapshot`)
            .then((response) => {
                setSnapshot(response.data);
            })
            .catch((reason) => {
                toast.error("Something went wrong", {toastId: "fetchingCurrentSnapshot"});
            });
    }

    const openTradeModal = (action , cryptoSymbol) => {
        var crypto = null;
        for(var item of snapshot)
            if(item.symbol === cryptoSymbol){
                crypto = item;
                break;
            }

        if(crypto == null) return;
        setCurrentTrade({ action, crypto});

        var minValue = 0.01
        if(crypto.price < 10)
            minValue = Math.ceil(0.01/crypto.price * 1000) / 1000
        setMinValueCrypto(minValue);

        if(action === "BUY"){
            setBalance(accountInfo.balance);
            if(formatQuantity(accountInfo.balance/crypto.price) == 0) setAmount(0.01);
            else setAmount(Math.min(minValue, Math.floor(accountInfo.balance/crypto.price * 1000) / 1000));
        } 
        else{
            var temp = accountInfo.holdings.find((holding) => holding.symbol === item.symbol).quantity;
            setBalance(temp);
            if(formatQuantity(temp) == 0) setAmount(0.01);
            else setAmount(Math.min(0.001, formatQuantity(temp)));
        }

        setShowModal(true);
    };

    const closeModal = () => {
        setShowModal(false);
        setCurrentTrade(null);
    };

    const handleAmountChange = (e) => {
        e.target.value = formatQuantity(e.target.value);
        e.target.value = Math.max(e.target.value, minValueCrypto);
        e.target.value = Math.min(e.target.value, 100000);
        setAmount(e.target.value);
    };

    const getConvertedAmount = () => {
        if (!currentTrade) return '';
        
        const amountNum = parseFloat(amount) || 0;
        var fiatValue, cryptoValue;
        cryptoValue = amountNum;
        fiatValue = formatMoney(amountNum * currentTrade.crypto.price);
        
        if(currentTrade.action === "BUY")
            ableToDoTransaction = (fiatValue <= availableBalance + 0.00001 ? true : false);
        else
            ableToDoTransaction = (cryptoValue <= availableBalance + 0.00001 ? true : false);

        return `$${fiatValue}`;
    };


    // Confirm trade
    const confirmTrade = () => {
        if (!currentTrade) return;
        
        const amountNum = parseFloat(amount) || 0;
        if (amountNum <= 0) {
            alert('Please enter a valid amount');
            return;
        }
        
        const cryptoAmount = amountNum;
        const fiatAmount = Math.ceil(amountNum * currentTrade.crypto.price * 100)/100;

        axios.post(api+'/transactions', {
                userId: 1,
                cryptoId: currentTrade.crypto.cryptoId,
                method: currentTrade.action,
                quantity: cryptoAmount,
                date: new Date().toISOString()
            }).then((response) => {
                toast.success(
                    "Successfully " + (response.data.method === "BUY" ? "bought" : "sold") + " " + formatQuantity(response.data.quantity) + " " + response.data.symbol);
                fetchCurrSnapshot();
            })
            .catch((reason) => {
                toast.error("Something went wrong");
                console.log(reason);
            });
        console.log(`${currentTrade.action} confirmed: ${cryptoAmount} ${currentTrade.crypto.symbol} ($${fiatAmount})`);
        closeModal();
    };

    const formatQuantity = (quantity) =>{
        return parseInt(quantity * 1000) / 1000 ;
    }

    const formatMoney = (a) =>{
        return parseFloat(a).toFixed(2);
    }
  
    if (accountInfo.length < 1) {
        return <div>Failed to load data.</div>; // Handle error case
    }
    
  return (
    <div id="market">
        <section class="market-section">
            <h3>Cryptocurrency Market</h3>
            <p class="subtitle">Trade top digital assets instantly</p>
            
            <table class="crypto-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Price</th>
                        <th>24h Change</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                {snapshot.map((item, index) => (
                  <tr key={index}>
                    <td>
                        <div class="crypto-name">
                            <span class="crypto-icon">
                              <img alt=""
                                src={`https://aps.fra1.cdn.digitaloceanspaces.com/img/${item.symbol.toLowerCase()}/small.png`}/>
                            </span>
                            {item.name} <span class="crypto-symbol">{item.symbol}</span>
                        </div>
                    </td>
                    <td class="price">${formatMoney(item.price)}</td>
                    <td class={"change "+item.changeType}>
                      <span>${formatMoney(item.change)}</span>&nbsp;
                      (<span>{formatMoney(item.changePct)}%</span>)
                    </td>
                    <td>
                        <div class="trade-actions">
                            <button 
                                class="btn btn-sm btn-success" 
                                onClick={() => openTradeModal('BUY', item.symbol)}>
                                <i class="fas fa-arrow-up"></i> Buy
                            </button>
                            <button 
                                class={"btn btn-sm btn-warning " + (accountInfo.holdings.find((holding) => holding.symbol === item.symbol) === undefined ? "disabled" : "")}
                                onClick={() => openTradeModal('SELL', item.symbol)}>
                                <i class="fas fa-arrow-down"></i> Sell
                            </button>
                        </div>
                    </td>
                  </tr>
                ))}
                </tbody>
            </table>
        </section>
        {currentTrade!=null &&
        <Modal show={showModal} onHide={closeModal} centered className="modal-sm">
            <Modal.Header closeButton>
            <Modal.Title>
                {currentTrade.action.charAt(0).toUpperCase() + currentTrade.action.slice(1).toLowerCase()} {currentTrade.crypto.name}
            </Modal.Title>
            </Modal.Header>
            <Modal.Body>
            <div className="balance-info p-3 mb-3 bg-light rounded">
                <div>Available: <strong>{displayBalance()}</strong></div>
            </div>
            
            <div className="amount-input-group position-relative mb-3">
                <span className="currency-label position-absolute start-0 top-50 translate-middle-y ms-3 transaction-holdings">
                    {<img alt="" class="crypto-icon" src={`https://aps.fra1.cdn.digitaloceanspaces.com/img/${currentTrade.crypto.symbol.toLowerCase()}/small.png`}/>}
                    {currentTrade.crypto.symbol}
                </span>
                <input
                    type="number"
                    pattern="[0-9.]"
                    className="form-control amount-input py-3 text-end"
                    value={amount}
                    onChange={handleAmountChange}
                    min={minValueCrypto}
                    max={10000}
                    step={step}/>
            </div>
            
            <div className="conversion-display text-end mb-4">
                ≈ {getConvertedAmount()}
            </div>
            <Button
                
                variant={currentTrade?.action === 'BUY' ? 'success' : 'warning'}
                size="lg"
                className={"w-100 py-2 "+(ableToDoTransaction ? "" : "disabled")}
                onClick={confirmTrade}
            >
                {currentTrade?.action ? `${currentTrade.action.charAt(0).toUpperCase() + (currentTrade.action.slice(1)).toLowerCase()} ${currentTrade.crypto.name}` : ''}
            </Button>
            </Modal.Body>
        </Modal>}
    </div>
  );
};