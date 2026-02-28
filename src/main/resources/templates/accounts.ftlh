<#import "layout/base.ftlh" as layout>

<@layout.page title="Accounts">

<div x-data="{
    accounts: [],
    customers: [],
    loading: true,
    error: null,
    async init() {
        try {
            const [acctData, custData] = await Promise.all([
                graphqlFetch('{ getAllAccounts { id accountType ownerId balance } }'),
                graphqlFetch('{ getAllCustomers { id name } }')
            ]);
            this.customers = custData.getAllCustomers || [];
            this.accounts = (acctData.getAllAccounts || []).map(a => ({
                ...a,
                ownerName: (this.customers.find(c => c.id == a.ownerId) || {}).name || 'Unknown'
            }));
        } catch (e) { this.error = e.message; }
        finally { this.loading = false; }
    }
}">

    <h1 class="mb-4">Accounts</h1>

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
                        <th>Owner</th>
                        <th>Type</th>
                        <th>Balance</th>
                    </tr>
                </thead>
                <tbody>
                    <template x-for="account in accounts" :key="account.id">
                        <tr>
                            <td x-text="account.id"></td>
                            <td>
                                <a :href="'/customers/' + account.ownerId" x-text="account.ownerName"></a>
                            </td>
                            <td><span class="badge text-bg-secondary" x-text="account.accountType"></span></td>
                            <td x-text="'$' + account.balance.toLocaleString()"></td>
                        </tr>
                    </template>
                </tbody>
            </table>
        </div>
    </template>

</div>

</@layout.page>
