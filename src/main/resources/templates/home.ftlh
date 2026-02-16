<#import "layout/base.ftlh" as layout>

<@layout.page title="Home">

<div x-data="{
    customers: [],
    accounts: [],
    cards: [],
    loading: true,
    error: null,
    showForm: false,
    newName: '',
    submitting: false,
    submitError: null,
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
    },
    async createCustomer() {
        this.submitting = true;
        this.submitError = null;
        try {
            const data = await graphqlFetch(`mutation($input: CreateCustomerAccountCardInput!) {
                createCustomerAccountCard(input: $input) {
                    customer { id name }
                    account { id }
                    card { id }
                    errors { message }
                }
            }`, {
                input: {
                    customer: { name: this.newName },
                    account: { accountType: 'CURRENT', balance: 0 },
                    card: { cardType: 'VIRTUAL' }
                }
            });
            const payload = data.createCustomerAccountCard;
            if (payload.errors.length > 0) {
                this.submitError = payload.errors.map(e => e.message).join(', ');
                return;
            }
            await this.init();
            this.showForm = false;
            this.newName = '';
        } catch (e) { this.submitError = e.message; }
        finally { this.submitting = false; }
    }
}">

    <div class="d-flex align-items-center mb-4">
        <h1 class="mb-0">Dashboard</h1>
        <button class="btn btn-primary ms-3" @click="showForm = true; submitError = null">New Customer</button>
    </div>

    <!-- Create Customer Modal -->
    <div class="modal fade" :class="{ show: showForm }" :style="showForm ? 'display:block' : 'display:none'" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">New Customer</h5>
                    <button type="button" class="btn-close" @click="showForm = false" :disabled="submitting"></button>
                </div>
                <div class="modal-body">
                    <template x-if="submitError">
                        <div class="alert alert-danger" x-text="submitError"></div>
                    </template>
                    <div class="mb-3">
                        <label for="customerName" class="form-label">Customer Name</label>
                        <input type="text" class="form-control" id="customerName" x-model="newName" :disabled="submitting"
                               @keydown.enter="newName.trim() && !submitting && createCustomer()">
                    </div>
                    <p class="text-muted small">A CURRENT account (balance $0) and VIRTUAL card will be created automatically.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" @click="showForm = false" :disabled="submitting">Cancel</button>
                    <button type="button" class="btn btn-primary" @click="createCustomer()" :disabled="submitting || !newName.trim()">
                        <span x-show="submitting" class="spinner-border spinner-border-sm me-1" role="status"></span>
                        Create
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal-backdrop fade show" x-show="showForm" style="display: none;"></div>

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
