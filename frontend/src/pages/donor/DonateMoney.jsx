import { useState } from 'react';
import { Link } from 'react-router-dom';
import monetaryApi from '../../api/monetaryApi';
import { notifyError } from '../../components/common/toast';
import { loadRazorpayScript } from '../../utils/loadRazorpayScript';

export default function DonateMoney() {
  const [form, setForm] = useState({ amount: '', purposeNote: '' });
  const [submitting, setSubmitting] = useState(false);
  const [successInfo, setSuccessInfo] = useState(null);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);

    const scriptLoaded = await loadRazorpayScript();
    if (!scriptLoaded) {
      notifyError('Could not load the payment gateway. Check your connection and try again.');
      setSubmitting(false);
      return;
    }

    let order;
    try {
      const orderResponse = await monetaryApi.createOrder({
        amount: Number(form.amount),
        purposeNote: form.purposeNote
      });
      order = orderResponse.data;
    } catch (err) {
      notifyError(err.message);
      setSubmitting(false);
      return;
    }

    const options = {
      key: order.razorpayKeyId,
      amount: order.amount,
      currency: order.currency,
      name: 'Aashray',
      description: form.purposeNote || 'Donation to Aashray',
      order_id: order.razorpayOrderId,
      prefill: {
        name: order.donorName,
        email: order.donorEmail
      },
      theme: { color: '#0d6efd' },
      handler: async (response) => {
        try {
          await monetaryApi.verifyPayment({
            razorpayOrderId: response.razorpay_order_id,
            razorpayPaymentId: response.razorpay_payment_id,
            razorpaySignature: response.razorpay_signature
          });
          setSuccessInfo({
            paymentId: response.razorpay_payment_id,
            amount: form.amount,
            referenceNumber: order.referenceNumber
          });
        } catch (err) {
          notifyError(
            err.message ||
              `We couldn't verify your payment. If money was deducted, keep this reference number and contact support: ${order.referenceNumber}`
          );
        } finally {
          setSubmitting(false);
        }
      },
      modal: {
        // Donor closed the checkout without completing payment.
        ondismiss: async () => {
          try {
            await monetaryApi.markPaymentFailed({
              razorpayOrderId: order.razorpayOrderId,
              reason: 'Checkout closed by donor'
            });
          } catch {
            // best-effort — donor already left the flow, nothing to show them
          }
          setSubmitting(false);
        }
      }
    };

    const razorpay = new window.Razorpay(options);

    razorpay.on('payment.failed', async (response) => {
      try {
        await monetaryApi.markPaymentFailed({
          razorpayOrderId: order.razorpayOrderId,
          reason: response.error?.description || 'Payment failed'
        });
      } catch {
        // best-effort
      }
      notifyError(response.error?.description || 'Payment failed. Please try again.');
      setSubmitting(false);
    });

    razorpay.open();
  };

  if (successInfo) {
    return (
      <div className="card auth-card">
        <div className="card-body p-4 text-center">
          <div className="display-6 text-success mb-2">✓</div>
          <h3 className="mb-2">Thank you for your donation!</h3>
          <p className="text-muted mb-4">Your payment of ₹{successInfo.amount} was successful.</p>
          <div className="text-start bg-light rounded p-3 mb-4">
            <div className="d-flex justify-content-between mb-1">
              <span className="text-muted">Transaction ID</span>
              <span className="fw-semibold">{successInfo.paymentId}</span>
            </div>
            <div className="d-flex justify-content-between">
              <span className="text-muted">Reference #</span>
              <span className="fw-semibold">{successInfo.referenceNumber}</span>
            </div>
          </div>
          <div className="d-flex gap-2 justify-content-center">
            <Link to="/donor/monetary-donations" className="btn btn-aashray">
              View My Donations
            </Link>
            <button
              className="btn btn-outline-secondary"
              onClick={() => {
                setSuccessInfo(null);
                setForm({ amount: '', purposeNote: '' });
              }}
            >
              Donate Again
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="card auth-card">
      <div className="card-body p-4">
        <h3 className="mb-4">Make a Donation</h3>
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label">Amount (₹)</label>
            <input
              type="number"
              min="1"
              step="0.01"
              className="form-control"
              name="amount"
              value={form.amount}
              onChange={handleChange}
              required
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Purpose (optional)</label>
            <input
              className="form-control"
              name="purposeNote"
              value={form.purposeNote}
              onChange={handleChange}
              maxLength={255}
              placeholder="e.g. For education support"
            />
          </div>
          <p className="text-muted small mb-3">
            You'll choose UPI, Card, Net Banking, or Wallet on the next screen — that's handled securely by Razorpay.
          </p>
          <button className="btn btn-aashray w-100" type="submit" disabled={submitting}>
            {submitting ? 'Processing...' : 'Donate Now'}
          </button>
        </form>
      </div>
    </div>
  );
}
