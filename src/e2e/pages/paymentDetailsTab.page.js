const config = require('../config');
const { I } = inject();
module.exports = {
fields: {
      refundsHeading: '//span[text()="Refunds"]',
   },
 async verifyPaymentDetails(){
      await I.wait(3);
 /*     To be amended after automation solution has been designed for payments tab.
      I.seeTextInTab(['Payment', 'Amount in pounds'], '183');
      I.seeTextInTab(['Payment', 'Channel'], 'online');
      I.seeTextInTab(['Payment', 'Created date'], '2 Nov 2021, 2:50:12 AM');
      I.seeTextInTab(['Payment', 'fee code'], 'A58');
      I.seeTextInTab(['Payment', 'Reference'], '1234');
      I.seeTextInTab(['Payment', 'Status'], 'Success');
      I.seeTextInTab(['Payment', 'Transaction Id'], '1234');
      I.seeTextInTab(['Payment', 'Updated date'], '2 Nov 2021, 2:50:12 AM');
     await I.retry(5).see(this.fields.refundsHeading);*/
  },
};
