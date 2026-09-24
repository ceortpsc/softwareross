import { readFile } from 'node:fs/promises';
import { createHash } from 'node:crypto';

const root = new URL('./', import.meta.url);
const manifest = JSON.parse(await readFile(new URL('brand-manifest.json', root), 'utf8'));
if (!/^[a-f0-9]{64}$/.test(manifest.logo.sha256)) throw new Error('Invalid logo SHA-256');
if (manifest.copyrightRegistrationFiled || manifest.trademarkRegistrationFiled) throw new Error('This release has no registration evidence');
if (manifest.nativeAppDeployBindingVerified) throw new Error('Native deployment binding is not verified');
if (process.argv[2]) {
  const bytes = await readFile(process.argv[2]);
  const actual = createHash('sha256').update(bytes).digest('hex');
  if (actual !== manifest.logo.sha256) throw new Error('Approved logo hash mismatch');
  console.log('Approved logo verified.');
} else {
  console.log('Manifest valid. No artwork file was supplied; binary verification not performed.');
}
