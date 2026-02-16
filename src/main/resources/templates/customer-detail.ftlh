<#import "layout/base.ftlh" as layout>

<@layout.page title="Customer Detail">

<div x-data="{
    customer: null,
    accounts: [],
    cards: [],
    loading: true,
    error: null,
    async init() {
        try {
            const customerId = ${customerId?c};
            const [custData, acctData, cardData] = await Promise.all([
                graphqlFetch('query($id: ID) { getCustomer(id: $id) { id name greeting } }', { id: customerId }),
                graphqlFetch('{ getAllAccounts { id accountType ownerId balance } }'),
                graphqlFetch('{ getAllCards { id cardHolderId cardType accountId } }')
            ]);
            this.customer = custData.getCustomer;
            this.accounts = (acctData.getAllAccounts || []).filter(a => a.ownerId == customerId);
            this.cards = (cardData.getAllCards || []).filter(c => c.cardHolderId == customerId);
        } catch (e) { this.error = e.message; }
        finally { this.loading = false; }
    }
}">

    <a href="/customers" class="btn btn-outline-secondary btn-sm mb-3">&larr; Back to Customers</a>

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

    <template x-if="!loading && !error && customer">
        <div>
            <h1 class="mb-2" x-text="customer.name"></h1>
            <p class="text-muted mb-4" x-text="customer.greeting"></p>

            <h3 class="mb-3">Accounts</h3>
            <template x-if="accounts.length === 0">
                <p class="text-muted">No accounts found.</p>
            </template>
            <template x-if="accounts.length > 0">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Type</th>
                            <th>Balance</th>
                        </tr>
                    </thead>
                    <tbody>
                        <template x-for="account in accounts" :key="account.id">
                            <tr>
                                <td x-text="account.id"></td>
                                <td><span class="badge text-bg-secondary" x-text="account.accountType"></span></td>
                                <td x-text="'$' + account.balance.toLocaleString()"></td>
                            </tr>
                        </template>
                    </tbody>
                </table>
            </template>

            <h3 class="mb-3 mt-4">Cards</h3>
            <template x-if="cards.length === 0">
                <p class="text-muted">No cards found.</p>
            </template>
            <template x-if="cards.length > 0">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Type</th>
                            <th>Account ID</th>
                        </tr>
                    </thead>
                    <tbody>
                        <template x-for="card in cards" :key="card.id">
                            <tr>
                                <td x-text="card.id"></td>
                                <td><span class="badge" :class="card.cardType === 'PHYSICAL' ? 'text-bg-primary' : 'text-bg-info'" x-text="card.cardType"></span></td>
                                <td x-text="card.accountId"></td>
                            </tr>
                        </template>
                    </tbody>
                </table>
            </template>
        </div>
    </template>

    <template x-if="!loading && !error && !customer">
        <div class="alert alert-warning">Customer not found.</div>
    </template>

</div>

</@layout.page>
