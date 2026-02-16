<#import "layout/base.ftlh" as layout>

<@layout.page title="Home">

<div x-data="{
    customers: [],
    accounts: [],
    cards: [],
    loading: true,
    error: null,
    async init() {
        try {
            const [custData, acctData, cardData] = await Promise.all([
                graphqlFetch('{ getAllCustomers { id name } }'),
                graphqlFetch('{ getAllAccounts { id balance } }'),
                graphqlFetch('{ getAllCards { id } }')
            ]);
            this.customers = custData.getAllCustomers || [];
            this.accounts = acctData.getAllAccounts || [];
            this.cards = cardData.getAllCards || [];
        } catch (e) { this.error = e.message; }
        finally { this.loading = false; }
    }
}">

    <h1 class="mb-4">Dashboard</h1>

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
            <div class="row mb-4">
                <div class="col-md-3">
                    <div class="card text-bg-primary">
                        <div class="card-body">
                            <h5 class="card-title">Customers</h5>
                            <p class="card-text display-6" x-text="customers.length"></p>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card text-bg-success">
                        <div class="card-body">
                            <h5 class="card-title">Accounts</h5>
                            <p class="card-text display-6" x-text="accounts.length"></p>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card text-bg-info">
                        <div class="card-body">
                            <h5 class="card-title">Cards</h5>
                            <p class="card-text display-6" x-text="cards.length"></p>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card text-bg-warning">
                        <div class="card-body">
                            <h5 class="card-title">Total Balance</h5>
                            <p class="card-text display-6" x-text="'$' + accounts.reduce((sum, a) => sum + a.balance, 0).toLocaleString()"></p>
                        </div>
                    </div>
                </div>
            </div>

            <h3 class="mb-3">Recent Customers</h3>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <template x-for="customer in customers" :key="customer.id">
                        <tr>
                            <td x-text="customer.id"></td>
                            <td x-text="customer.name"></td>
                            <td>
                                <a :href="'/customers/' + customer.id" class="btn btn-sm btn-outline-primary">View</a>
                            </td>
                        </tr>
                    </template>
                </tbody>
            </table>
        </div>
    </template>

</div>

</@layout.page>
