(function () {
    'use strict';

// detects whether a cell contains a date, number or plain text
    function detectType(value) {
        if (/^\d{4}-\d{2}-\d{2}$/.test(value)) return 'date';
        if (/^[\d.,]+(\s*EUR)?$/.test(value.trim())) return 'number';
        return 'text';
    }

    // extracts content from specific cell in a row
    function getCellText(row, colIndex) {
        const cell = row.cells[colIndex];
        return cell ? cell.textContent.trim() : '';
    }

    // default sort direction by type: dates -> newest first (desc), everything else → A–Z (asc)
    function defaultDir(type) {
        return type === 'date' ? 'desc' : 'asc';
    }

    // sorting handler
    function sortTable(table, clickedTh, colIndex) {
        const tbody  = table.querySelector('tbody');
        const rows   = Array.from(tbody.querySelectorAll('tr')).filter(
            r => !r.querySelector('td.table-empty')
        );
        if (rows.length < 2) return;

        // Sample column type from first non-empty cell
        const sample = rows.map(r => getCellText(r, colIndex)).find(v => v) || '';
        const type   = detectType(sample);

        // Determine new direction
        const prev   = clickedTh.dataset.sortDir || 'none';
        const newDir = prev === 'none' ? defaultDir(type) : (prev === 'asc' ? 'desc' : 'asc');

        // Reset all header indicators in this table
        table.querySelectorAll('thead th.sortable-col').forEach(th => {
            th.dataset.sortDir = 'none';
            const icon = th.querySelector('.sort-icon');
            if (icon) { icon.className = 'bi bi-arrow-down-up sort-icon ms-1'; }
        });

        // Apply new state to clicked header
        clickedTh.dataset.sortDir = newDir;
        const icon = clickedTh.querySelector('.sort-icon');
        if (icon) {
            icon.className = `bi ${newDir === 'asc' ? 'bi-arrow-up' : 'bi-arrow-down'} sort-icon ms-1 active`;
        }

        // Sort rows
        rows.sort((a, b) => {
            const av = getCellText(a, colIndex);
            const bv = getCellText(b, colIndex);
            let cmp;
            if (type === 'date') {
                cmp = new Date(av) - new Date(bv);
            } else if (type === 'number') {
                cmp = parseFloat(av.replace(/[^\d.]/g, '') || 0)
                    - parseFloat(bv.replace(/[^\d.]/g, '') || 0);
            } else {
                cmp = av.localeCompare(bv, undefined, { sensitivity: 'base' });
            }
            return newDir === 'asc' ? cmp : -cmp;
        });

        rows.forEach(r => tbody.appendChild(r));
    }

    // initializes tables by finding every .data-table, adds sortable-col
    function initTables() {
        document.querySelectorAll('.data-table').forEach(table => {
            table.querySelectorAll('thead th').forEach((th, colIndex) => {
                if (th.textContent.trim().toLowerCase() === 'actions') return;

                th.classList.add('sortable-col');
                th.dataset.sortDir = 'none';

                // Append sort icon
                const icon = document.createElement('i');
                icon.className = 'bi bi-arrow-down-up sort-icon ms-1';
                th.appendChild(icon);

                th.addEventListener('click', () => sortTable(table, th, colIndex));
            });
        });
    }

    document.addEventListener('DOMContentLoaded', initTables);
})();
