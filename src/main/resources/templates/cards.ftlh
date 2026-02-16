<#import "layout/base.ftlh" as layout>

<@layout.page title="Cards">

<div x-data="{
    cards: [],
    customers: [],
    loading: true,
    error: null,
    async init() {
        try {
            const [cardData, custData] = await Promise.all([
                graphqlFetch('{ getAllCards { id cardHolderId cardType accountId account { id accountType balance } } }'),
                graphqlFetch('{ getAllCustomers { id name } }')
            ]);
            this.customers = custData.getAllCustomers || [];
            this.cards = (cardData.getAllCards || []).map(c => ({
                ...c,
                holderName: (this.customers.find(cu => cu.id == c.cardHolderId) || {}).name || 'Unknown'
            }));
        } catch (e) { this.error = e.message; }
        finally { this.loading = false; }
    }
}">

    <h1 class="mb-4">Cards</h1>

    <template x-if="loading">
        <div class="text-center py-5">
            <div class="spinner-border" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    </template>

    <template x-if="error">
        <div class="alert alert-danger" x-text="error"></div>
    </template>

    <template x-if="!loading && !error">
        <div>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Card Holder</th>
                        <th>Card Type</th>
                        <th>Account</th>
                        <th>Account Type</th>
                        <th>Balance</th>
                    </tr>
                </thead>
                <tbody>
                    <template x-for="card in cards" :key="card.id">
                        <tr>
                            <td x-text="card.id"></td>
                            <td>
                                <a :href="'/customers/' + card.cardHolderId" x-text="card.holderName"></a>
                            </td>
                            <td><span class="badge" :class="card.cardType === 'PHYSICAL' ? 'text-bg-primary' : 'text-bg-info'" x-text="card.cardType"></span></td>
                            <td x-text="card.account ? card.account.id : card.accountId"></td>
                            <td>
                                <template x-if="card.account">
                                    <span class="badge text-bg-secondary" x-text="card.account.accountType"></span>
                                </template>
                            </td>
                            <td x-text="card.account ? '$' + card.account.balance.toLocaleString() : '-'"></td>
                        </tr>
                    </template>
                </tbody>
            </table>
        </div>
    </template>

</div>

</@layout.page>
