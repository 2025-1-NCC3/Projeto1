const sqlite3 = require("sqlite3").verbose();
const path = require("path");
const dbPath = path.join(__dirname, "BancoDeDados.db");

const db = new sqlite3.Database(dbPath, (err) => {
  if (err) console.error("❌ ERRO AO CONECTAR AO BANCO DE DADOS:", err.message);
  else console.log("✅ BANCO DE DADOS CONECTADO COM SUCESSO");
});

module.exports = db;
